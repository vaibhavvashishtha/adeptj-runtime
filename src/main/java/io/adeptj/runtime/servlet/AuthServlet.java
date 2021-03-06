/*
###############################################################################
#                                                                             # 
#    Copyright 2016, AdeptJ (http://www.adeptj.com)                           #
#                                                                             #
#    Licensed under the Apache License, Version 2.0 (the "License");          #
#    you may not use this file except in compliance with the License.         #
#    You may obtain a copy of the License at                                  #
#                                                                             #
#        http://www.apache.org/licenses/LICENSE-2.0                           #
#                                                                             #
#    Unless required by applicable law or agreed to in writing, software      #
#    distributed under the License is distributed on an "AS IS" BASIS,        #
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. #
#    See the License for the specific language governing permissions and      #
#    limitations under the License.                                           #
#                                                                             #
###############################################################################
*/

package io.adeptj.runtime.servlet;

import io.adeptj.runtime.common.RequestUtil;
import io.adeptj.runtime.common.ResponseUtil;
import io.adeptj.runtime.mvc.TemplateData;
import io.adeptj.runtime.mvc.TemplateContext;
import io.adeptj.runtime.mvc.TemplateEngine;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.adeptj.runtime.common.Constants.OSGI_ADMIN_ROLE;
import static io.adeptj.runtime.common.Constants.TOOLS_DASHBOARD_URI;
import static io.adeptj.runtime.common.Constants.TOOLS_LOGIN_URI;
import static io.adeptj.runtime.common.Constants.TOOLS_LOGOUT_URI;

/**
 * AuthServlet does the following:
 * <p>
 * 1. Renders the login page and handles the validation failure on wrong credentials submission.
 * 2. Logout the currently logged in Admin user and renders the login page again.
 * <p>
 * Note: This is independent of OSGi and directly managed by Undertow.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
@WebServlet(
        name = "AdeptJ AuthServlet",
        urlPatterns = {
                TOOLS_LOGIN_URI,
                TOOLS_LOGOUT_URI
        },
        asyncSupported = true
)
public class AuthServlet extends HttpServlet {

    private static final long serialVersionUID = -3339904764769823449L;

    private static final String LOGIN_TEMPLATE = "auth/login";

    private static final String J_USERNAME = "j_username";

    private static final String ERROR_MSG_KEY = "error";

    private static final String ERROR_MSG = "Invalid credentials!!";

    /**
     * Render login page.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String requestURI = req.getRequestURI();
        if (TOOLS_LOGIN_URI.equals(requestURI)) {
            TemplateEngine.getInstance().render(TemplateContext.builder()
                    .request(req)
                    .response(resp)
                    .template(LOGIN_TEMPLATE)
                    .locale(req.getLocale())
                    .build());
        } else if (TOOLS_LOGOUT_URI.equals(requestURI) && req.isUserInRole(OSGI_ADMIN_ROLE)) {
            // Invalidate the session and redirect to /tools/dashboard page.
            RequestUtil.logout(req);
            ResponseUtil.redirect(resp, TOOLS_DASHBOARD_URI);
        } else {
            // if someone requesting logout URI anonymously, which doesn't make sense. Redirect to /tools/dashboard.
            ResponseUtil.redirect(resp, TOOLS_DASHBOARD_URI);
        }
    }

    /**
     * Handle "/auth/j_security_check" validation failure.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        // Render login page again with validation message.
        TemplateEngine.getInstance().render(TemplateContext.builder()
                .request(req)
                .response(resp)
                .template(LOGIN_TEMPLATE)
                .locale(req.getLocale())
                .templateData(TemplateData.newTemplateData()
                        .put(ERROR_MSG_KEY, ERROR_MSG)
                        .put(J_USERNAME, req.getParameter(J_USERNAME)))
                .build());
    }
}