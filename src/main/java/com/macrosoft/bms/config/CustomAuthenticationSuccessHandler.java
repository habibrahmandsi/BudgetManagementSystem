package com.macrosoft.bms.config;

import com.macrosoft.bms.data.dao.Impl.AdminDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
    @Autowired
    private AdminDAOImpl envityDaoImpl;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            HttpSession session = request.getSession();
            if (session != null) {
                String redirectUrl = (String) session.getAttribute("url_prior_login");
                if (redirectUrl != null) {
                    session.removeAttribute("url_prior_login");
                    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                } else {
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
            return;
        }

        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
