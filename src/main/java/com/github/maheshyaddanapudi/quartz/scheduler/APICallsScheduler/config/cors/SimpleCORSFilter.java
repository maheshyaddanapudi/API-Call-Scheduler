package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.config.cors;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Profile({Constants.EXTERNAL_ADFS, Constants.EXTERNAL_OAUTH2, Constants.EMBEDDED_OAUTH2})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class SimpleCORSFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        log.debug("Starting SimpleCORSFilter");
        if(Constants.OPTIONS.equalsIgnoreCase(request.getMethod()))
        {
            log.debug("Applying SimpleCORSFilter. Options Header Found ...");
            if(null==response.getHeaders("Access-Control-Allow-Origin") || response.getHeaders("Access-Control-Allow-Origin").isEmpty())
                response.setHeader("Access-Control-Allow-Origin", "*");
            if(null==response.getHeaders("Access-Control-Allow-Methods") || response.getHeaders("Access-Control-Allow-Methods").isEmpty())
                response.setHeader("Access-Control-Allow-Methods", "GET,POST,PATCH,PUT,DELETE,OPTIONS,HEAD");
            if(null==response.getHeaders("Access-Control-Allow-Headers") || response.getHeaders("Access-Control-Allow-Headers").isEmpty())
                response.setHeader("Access-Control-Allow-Headers", "*");

            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            log.debug("Skipping SimpleCORSFilter. Options Header NOT Found ...");
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
