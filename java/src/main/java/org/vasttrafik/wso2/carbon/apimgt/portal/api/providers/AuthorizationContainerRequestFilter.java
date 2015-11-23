/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vasttrafik.wso2.carbon.apimgt.portal.api.providers;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Authorization;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.resources.Security;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Authorization
@Provider
public class AuthorizationContainerRequestFilter implements ContainerRequestFilter, ResourceBundleAware {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            final String authorization = requestContext.getHeaderString("Authorization");
            final String username = Security.validateToken(authorization);
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                            return username;
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String s) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return null;
                }
            });
        } catch (final Exception exception) {
            requestContext.abortWith(ResponseUtils.notAuthorizedError(resourceBundle, 2003L, new Object[][]{}));
        }
    }

}