package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.ws.rs.NotAuthorizedException;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.security.Security;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.clients.ProxyClient;
import org.vasttrafik.wso2.carbon.common.api.beans.AuthenticatedUser;
import org.vasttrafik.wso2.carbon.common.api.impl.AbstractApiServiceImpl;
import org.vasttrafik.wso2.carbon.common.api.utils.AbstractErrorListResourceBundle;
import org.vasttrafik.wso2.carbon.common.api.utils.ClientUtils;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

// TO-DO: Refactor into impl package when resorces are refactored into using impl delegate classes
public class PortalResource extends AbstractApiServiceImpl implements ResourceBundleAware {

	@Override
	protected ResponseUtils getResponseUtils() {
		// Load the resource bundle
		AbstractErrorListResourceBundle	bundle = (AbstractErrorListResourceBundle)
				ResourceBundle.getBundle(resourceBundle, new Locale("sv", "SE"), getClass().getClassLoader());

		// Create a ResponseUtils instance
		return new ResponseUtils(bundle);
	}
	
	protected ProxyClient getProxyClient(final String authorization) throws Exception {
        if (authorization == null) {
            return new ProxyClient(ClientUtils.ADMIN_USER_NAME, ClientUtils.ADMIN_PASSWORD);
        }
        else {
            final String userName;
            
            try {
            	AuthenticatedUser user = authorize(authorization);
				userName = user.getUserName();
            } 
            catch (Exception e) {
                throw new NotAuthorizedException(ResponseUtils.notAuthorizedError(resourceBundle, 2003L, new Object[][]{}));
            }
            return Security.getClient(userName);
        }
    }
}
