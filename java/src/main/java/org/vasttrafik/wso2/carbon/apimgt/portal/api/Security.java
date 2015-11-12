package org.vasttrafik.wso2.carbon.apimgt.portal.api;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.AccessToken;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.AuthenticatedUser;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Credentials;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.clients.TokenClient;
import org.vasttrafik.wso2.carbon.identity.api.utils.UserAdminUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Path("security")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Security {

    /**
     * This map contains user names mapped to access token instances.
     * The map may contain expired access tokens since the user may not log out.
     * There can never be more entries in the map than registered users
     */
    public static final Map<String, AccessToken> ACCESS_TOKENS = new ConcurrentHashMap<>();

    public static final String DEFAULT_APPLICATION = "DefaultApplication";

    @POST
    public Response postSecurity(
            final Credentials credentials,
            @QueryParam("action") final String action,
            @QueryParam("refreshToken") final String refreshToken,
            @HeaderParam("Authorization") final String authorization
    ) {
        switch (StringUtils.defaultString(action)) {
            case "login":
                return login(authorization, credentials);
            case "logout":
                return logout(authorization);
            case "refreshToken":
                return refreshToken(refreshToken);
        }
        throw new BadRequestException("action missing or invalid");
    }

    private Response login(final String authorization, final Credentials credentials) {
        try {
            if (credentials == null || credentials.getUserName() == null || credentials.getCredential() == null) {
                throw new BadRequestException("credentials not provided");
            }
            final String userName = credentials.getUserName();
            final String credential = credentials.getCredential();

            /**
             * Check if the user is already logged in.
             * This block might return from the method.
             */
            try {
                final String existingUserName = UserAdminUtils.validateToken(authorization);

                if (!userName.equals(existingUserName)) {
                    throw new BadRequestException("credentials does not match authorization header");
                }

                final int userId = UserAdminUtils.getUserId(userName);
                return Response.status(Status.CREATED)
                        .entity(new AuthenticatedUser("" + userId, userName, ACCESS_TOKENS.get(userName)))
                        .build();
            } catch (final Exception exception) {
            }

            /**
             * This will throw NotAuthorizedException if the authentication fails.
             * The purpose of the rest of the method is to generate a response.
             */
            UserAdminUtils.authenticateCredentials(userName, credential);

            /**
             * Get an existing generated token to use
             */
            final TokenClient tokenClient = new TokenClient();
            tokenClient.initialize(DEFAULT_APPLICATION);
            final String token = tokenClient.getToken();
            final int expires = tokenClient.getExpires();

            /**
             * Create a AccessToken instance and store in the ACCESS_TOKENS map
             */
            final AccessToken accessToken = new AccessToken(token, null, null); // TODO: refreshToken & expiresIn
            ACCESS_TOKENS.put(userName, accessToken);

            /**
             * Return with an AuthenticatedUser instance
             */
            final int userId = UserAdminUtils.getUserId(credentials.getUserName());
            return Response.status(Status.CREATED).entity(
                    new AuthenticatedUser("" + userId, userName, accessToken)
            ).build();
        } catch (final NotAuthorizedException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new NotAuthorizedException(ExceptionUtils.getStackTrace(exception));
        }
    }

    private Response logout(final String authorization) {
        try {
            final String userName = UserAdminUtils.validateToken(authorization);
            ACCESS_TOKENS.remove(userName);
            return Response.noContent().build();
        } catch (final Exception exception) {
            throw new NotAuthorizedException(ExceptionUtils.getStackTrace(exception));
        }
    }

    private Response refreshToken(final String refreshToken) {
        throw new ServerErrorException(Status.NOT_IMPLEMENTED);
    }

}
