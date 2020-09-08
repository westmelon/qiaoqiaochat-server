package com.neo.qiaoqiaochat.web.auth;

import com.neo.qiaoqiaochat.web.datasource.MultipleDataSourceAspect;
import com.neo.qiaoqiaochat.web.datasource.MultipleDataSourceHolder;
import com.neo.qiaoqiaochat.web.datasource.TargetDataSource;
import com.neo.qiaoqiaochat.web.service.impl.AuthService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Order(-998)// 保证该AOP在@Transactional之前执行
@Component
public class UserAuthAspect {

    private final Logger logger = LoggerFactory.getLogger(UserAuthAspect.class);

    @Autowired
    private AuthService authService;

    @Around("@annotation(userAuth)")
    public Object verifyAuth(ProceedingJoinPoint point, UserAuth userAuth) throws Throwable {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String token = request.getHeader("token");
        logger.info(token);

        authService.checkTokenValid(token);

        Object result = point.proceed(point.getArgs());


        return result;
    }
}
