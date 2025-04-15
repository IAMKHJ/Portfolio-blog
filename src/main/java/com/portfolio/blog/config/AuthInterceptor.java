package com.portfolio.blog.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("==================== START ====================");
        log.info("===============================================");
        log.info(" Request URI : {}", request.getRequestURI());

        HttpSession session = request.getSession();
        String url = request.getRequestURL().toString();
        Object user = session.getAttribute("USER");

        if(user==null){
            response.sendRedirect("/login");
            return false;
        }
        if(url.contains("/null")){
            response.sendRedirect("/");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("===============================================");
        log.info("==================== END ======================");
    }

}
