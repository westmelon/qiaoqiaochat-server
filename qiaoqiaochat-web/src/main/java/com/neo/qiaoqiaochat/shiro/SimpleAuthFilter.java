package com.neo.qiaoqiaochat.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleAuthFilter extends FormAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(SimpleAuthFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login submission detected.  Attempting to execute login.");
                }

                return this.executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login page view.");
                }

                return true;
            }
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
            }

            this.saveRequestAndWriteResponse(request, response);
            return false;
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }


    private void saveRequestAndWriteResponse(ServletRequest request, ServletResponse response) {
        this.saveRequest(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        response.setContentType("application/json; charset=UTF-8");
        String msg = "{\"code\":401,\"msg\":\"未登录\"}";

        try {
            response.getWriter().write(msg);
            response.getWriter().flush();
        } catch (IOException var6) {
            logger.error("saveRequestAndWriteResponse error:" + var6.getMessage());
        }

    }
}
