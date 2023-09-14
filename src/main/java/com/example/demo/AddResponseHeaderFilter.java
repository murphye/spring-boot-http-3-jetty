package com.example.demo;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AddResponseHeaderFilter implements Filter {

    @Value( "${server.port}" )
    private Integer serverPort;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(
                "Alt-Svc", "h3=\":" + serverPort + "\"; ma=86400; persist=1");
        chain.doFilter(request, response);
    }
}
