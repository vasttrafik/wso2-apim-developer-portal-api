package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class AuthenticatedUser {

    private String userId;
    private String userName;
    private AccessToken accessToken;

    public AuthenticatedUser(String userId, String userName, AccessToken accessToken) {
        this.userId = userId;
        this.userName = userName;
        this.accessToken = accessToken;
    }

}
