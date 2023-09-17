package com.example.demo;

import org.eclipse.jetty.http3.server.HTTP3ServerConnectionFactory;
import org.eclipse.jetty.http3.server.HTTP3ServerConnector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.boot.web.embedded.jetty.JettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Paths;

@Configuration
public class JettyConfiguration implements WebServerFactoryCustomizer<JettyReactiveWebServerFactory> {

    @Autowired
    private DefaultSslBundleRegistry defaultSslBundleRegistry;

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${server.jetty.connection-idle-timeout}")
    private Integer idleTimeout;

    @Override
    public void customize(JettyReactiveWebServerFactory factory) {
        var jettyServerCustomizer = new JettyServerCustomizer() {
            @Override
            public void customize(Server server) {
                var keyStore = defaultSslBundleRegistry.getBundle("server").getStores().getKeyStore();

                SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
                sslContextFactory.setKeyStore(keyStore);
                sslContextFactory.setKeyStorePassword(""); // Must be set for Jetty

                HttpConfiguration httpConfig = new HttpConfiguration();
                httpConfig.addCustomizer(new SecureRequestCustomizer());
                httpConfig.setIdleTimeout(idleTimeout);

                HTTP3ServerConnector connector = new HTTP3ServerConnector(server, sslContextFactory, new HTTP3ServerConnectionFactory(httpConfig));
                connector.getQuicConfiguration().setPemWorkDirectory( // Must be set for Jetty
                        Paths.get(System.getProperty("java.io.tmpdir"))); // Default system temp directory

                connector.setPort(serverPort);
                server.addConnector(connector);
            }
        };

        factory.addServerCustomizers(jettyServerCustomizer);
    }
}
