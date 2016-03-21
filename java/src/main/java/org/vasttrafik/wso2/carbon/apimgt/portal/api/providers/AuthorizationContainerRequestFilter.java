/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vasttrafik.wso2.carbon.apimgt.portal.api.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.vasttrafik.wso2.carbon.identity.oauth.authcontext.JWTClaims;
import org.vasttrafik.wso2.carbon.identity.oauth.authcontext.JWTTokenValidator;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Authorization;
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
	
	private static final Log log = LogFactory.getLog(AuthorizationContainerRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) {
    	final String authorization = requestContext.getHeaderString("X-JWT-Assertion");
    	
        try {
            final JWTClaims claims = validateToken(authorization);
            
            requestContext.setSecurityContext(new SecurityContext() {	
            	private JWTClaims jwtClaims = claims;
            	
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                        	return claims.getEndUser();
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String s) {
                	String[] roles = jwtClaims.getUserRoles();
                	for (String role : roles)
                		if (role.equalsIgnoreCase(s))
                			return true;
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
        }
        catch (final Exception exception) {
        	// Get error code
        	final String message = exception.getMessage();
        	Long code;
        	
        	try {
        		code = Long.parseLong(message);
        	}
        	catch (Exception e) {
        		code = 2003L;
        	}
        	
        	// Abort
        	requestContext.abortWith(ResponseUtils.notAuthorizedError(
        			resourceBundle, code, new Object[][]{}));
        } 
    }
    
    private JWTClaims validateToken(String token) throws Exception {
    	JWTTokenValidator jwtValidator = new JWTTokenValidator(token);
		
		if (jwtValidator.isValid()) {
			// Get the claims object
			JWTClaims claims = jwtValidator.getJWTClaims();
		
			// Make sure the token hasn't expired
			if (claims.hasExpired()) {
				throw new Exception("1003");
			}
			else
			if (claims.isAccountLocked()) {
				throw new Exception("1007");
			}
			return claims;
		}
		else
			log.error("Invalid JWT token:" + token);
		
		throw new Exception("2003");
    }

}