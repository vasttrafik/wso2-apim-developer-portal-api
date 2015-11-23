package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class AuthenticatedUser {

    private String userId;
    private String userName;
    private AccessToken accessToken;

    public static AuthenticatedUser valueOf(final Integer userId, final String userName, final AccessToken accessToken) {
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.userId = String.valueOf(userId);
        authenticatedUser.userName = userName;
        authenticatedUser.accessToken = accessToken;
        return authenticatedUser;
    }

}
