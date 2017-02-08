package com.macrosoft.bms.config;

import com.macrosoft.bms.util.Constants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Ajax Aware Authentication Configuration.
 * @author Habibur Rahaman Sumon
 */

public class AjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint
{
    public AjaxAwareAuthenticationEntryPoint(String loginUrl) {
        super(loginUrl);
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        String ajaxHeader = ((HttpServletRequest) request).getHeader(Constants.AJAX_COMMON_REQUEST_HEADER);
        if ("XMLHttpRequest".equals(ajaxHeader)) {
            System.out.println("***************** AJAX request failed for session timeout *****************");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Ajax Request Denied (Session Expired). Please login first.");
        } else {
            super.commence(request, response, authException);
        }
    }
}