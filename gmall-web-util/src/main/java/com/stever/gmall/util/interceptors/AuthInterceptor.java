package com.stever.gmall.util.interceptors;

import com.stever.gmall.util.CookieUtil;
import com.stever.gmall.util.annotations.LoginRequired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 拦截代码


        // 查看是否有需要拦截的注解
        HandlerMethod hm = (HandlerMethod)handler;
        LoginRequired loginRequired = hm.getMethodAnnotation(LoginRequired.class);
        if(loginRequired==null){
            return true;
        }
        boolean value = loginRequired.value();
        if(value==false){
            return true;
        }
        String newToken = request.getParameter("newToken");
        if(newToken!=null && newToken.length()>0){
            CookieUtil.setCookie(request, response, "token", newToken, 60*3600, false);
        }

        return true;
    }
}
