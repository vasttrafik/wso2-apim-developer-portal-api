package org.vasttrafik.wso2.carbon.apimgt.portal.api.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vasttrafik.wso2.carbon.common.api.beans.Credentials;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.clients.ProxyClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Security implements ResourceBundleAware {

    private static final Map<String, ProxyClient> SESSIONS = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(Security.class);

    public static ProxyClient getClient(final String username) {
        return SESSIONS.get(username.toLowerCase());
    }

    
    public static void login(final Credentials credentials) {
    	String userName = credentials.getUserName().toLowerCase();  
        SESSIONS.put(userName, new ProxyClient(userName, credentials.getCredential()));
    }

}
