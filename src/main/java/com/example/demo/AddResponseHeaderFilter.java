package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AddResponseHeaderFilter implements WebFilter {

    @Value( "${server.port}" )
    private Integer serverPort;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse()
                .getHeaders().add("Alt-Svc", "h3=\":" + serverPort + "\"; ma=86400; persist=1");
        return chain.filter(exchange);
    }
}
