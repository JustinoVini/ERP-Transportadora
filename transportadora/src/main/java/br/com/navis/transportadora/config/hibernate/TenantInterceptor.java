package br.com.navis.transportadora.config.hibernate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Optional;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String[] PUBLIC_MATCHERS = { "/transportadora/*", "/status/*" };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String requestURI = request.getRequestURI();

        if (Arrays.asList(PUBLIC_MATCHERS).contains(requestURI)) return true;

        Optional.ofNullable(request.getHeader("tenant"))
                .map(String::toUpperCase)
                .ifPresent(TenantContext::setCurrentTenant);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TenantContext.clear();
    }
}
