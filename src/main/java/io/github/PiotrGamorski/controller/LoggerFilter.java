package io.github.PiotrGamorski.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
class LoggerFilter implements Filter{
    Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if(request instanceof HttpServletRequest){
                var httpRequest = (HttpServletRequest) request;
                logger.info("[doFilter] " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());
            }
        } finally {
            chain.doFilter(request, response);
        }
    }
}
