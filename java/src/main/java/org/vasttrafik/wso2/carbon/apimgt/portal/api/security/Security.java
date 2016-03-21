package org.vasttrafik.wso2.carbon.apimgt.portal.api.security;

//import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.AccessToken;
//import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.AuthenticatedUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vasttrafik.wso2.carbon.common.api.beans.Credentials;
import org.vasttrafik.wso2.carbon.identity.oauth.authcontext.JWTTokenGenerator;
//import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.OauthData;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.clients.ProxyClient;
//import org.vasttrafik.wso2.carbon.apimgt.token.api.clients.TokenClient;
//import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;
//import org.vasttrafik.wso2.carbon.identity.api.utils.UserAdminUtils;


//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
//@Path("security")
public class Security implements ResourceBundleAware {

    //private static final String TOKEN_VALIDITY_TIME = "3600";

    /*private class Session {
        private AccessToken accessToken;
        private OauthData oauthData;
        private ProxyClient proxyClient;
    }*/

    //private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();
    private static final Map<String, ProxyClient> SESSIONS = new ConcurrentHashMap<>();
    
    private static final Log log = LogFactory.getLog(Security.class);

    /**
     * Validate authorization header value
     * If the given Authorization header value represents a user with a active session this method returns the username
     * @param authorization The authorization header value. E.g. "Bearer d5e18e881d64802728e4193eeac18a38"
     * @return The username of the authorized user.
     */
    /*public static String validateToken(final String authorization) throws Exception {
        final String username = UserAdminUtils.validateToken(authorization);
        if (SESSIONS.containsKey(username)) {
            return username;
        }
        throw new NotAuthorizedException(null);
    }*/

    public static ProxyClient getClient(final String username) {
        //return SESSIONS.get(username).proxyClient;
        return SESSIONS.get(username);
    }
    /*
    @POST
    public Response postSecurity(
            final Credentials credentials,
            @QueryParam("action") final String action,
            @QueryParam("refreshToken") final String refreshToken,
            @HeaderParam("Authorization") final String authorization
    ) {
        ResponseUtils.checkParameter(resourceBundle, "action", true, new String[]{"login", "logout", "refreshToken"}, action);

        try {
            switch (action) {
                case "login":
                    return login(authorization, credentials);
                case "logout":
                    return logout(authorization);
                case "refreshToken":
                    return login(authorization, credentials); // Refresh Token is currently not supported.
                default:
                    throw new IllegalStateException();
            }
        } catch (final BadRequestException | NotAuthorizedException | NotFoundException | ForbiddenException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(exception);
        }
    }*/

    /*private Response login(final String authorization, final Credentials credentials) {
        try {
            if (credentials == null || credentials.getUserName() == null || credentials.getCredential() == null) {
                throw new BadRequestException(ResponseUtils.badRequest(resourceBundle, 2004L, new Object[][]{}));
            }
            final String userName = credentials.getUserName().toLowerCase();
            final String credential = credentials.getCredential();
			
			if ("admin".equalsIgnoreCase(userName))
				throw new ForbiddenException();

             // Check if the user is already logged in.
             // This block might return from the method.
            try {
                final String existingUserName = UserAdminUtils.validateToken(authorization);

                if (!userName.equals(existingUserName)) {
                    throw new BadRequestException(ResponseUtils.badRequest(resourceBundle, 2005L, new Object[][]{}));
                }

                final Session session = SESSIONS.get(userName);
                if (session != null) {
                    final int userId = UserAdminUtils.getUserId(userName);
                    return Response.status(Status.CREATED).entity(
                            AuthenticatedUser.valueOf(userId, userName, session.accessToken)
                    ).build();
                }
            } catch (final Exception exception) {
            }

             // This will throw NotAuthorizedException if the authentication fails.
             // The purpose of the rest of the method is to generate a response.
            UserAdminUtils.authenticateCredentials(userName, credential);

            // Create a proxy client instance
            final ProxyClient proxyClient = new ProxyClient(userName, credential);

            // Get refresh a new token using the default application key and secret
            OauthData oauthData = proxyClient.getDefaultApplicationOauthData();
            oauthData.setValidityTime(TOKEN_VALIDITY_TIME);
            oauthData = proxyClient.generateDefaultApplicationOauthData(oauthData);
            final AccessToken accessToken = AccessToken.valueOf(oauthData.getToken(), null, oauthData.getValidityTime());

            // Store proxy, oauthData and accessToken in SESSIONS
            final Session session = new Session();
            session.proxyClient = proxyClient;
            session.oauthData = oauthData;
            session.accessToken = accessToken;
            SESSIONS.put(userName, session);

            // Return with an AuthenticatedUser instance
            final int userId = UserAdminUtils.getUserId(credentials.getUserName());
            return Response.status(Status.CREATED).entity(
                    AuthenticatedUser.valueOf(userId, userName, accessToken)
            ).build();
        } catch (final NotAuthorizedException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new NotAuthorizedException(ResponseUtils.notAuthorizedError(exception));
        }
    }*/
    
    public static void login(final Credentials credentials) {
    	String userName = credentials.getUserName().toLowerCase();  
        ProxyClient proxyClient = SESSIONS.get(userName);
        
        if (proxyClient == null) {
            SESSIONS.put(userName, new ProxyClient(userName, credentials.getCredential()));
        }
    }

    /*private Response logout(final String authorization) {
        try {
            final String userName = UserAdminUtils.validateToken(authorization);
            final OauthData oauthData = SESSIONS.get(userName).oauthData;
            SESSIONS.remove(userName);
            new TokenClient().revoke(oauthData.getToken(), oauthData.getKey(), oauthData.getSecret());
            return Response.noContent().build();
        } catch (final Exception exception) {
            throw new NotAuthorizedException(ResponseUtils.notAuthorizedError(exception));
        }
    }*/

}
