package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class AuthenticatedUser {

	private String userId;
	private String userName;
	private AccessToken accessToken;

	public AuthenticatedUser() {
	}

	public AuthenticatedUser(String userId, String userName, AccessToken accessToken) {
		this.userId = userId;
		this.userName = userName;
		this.accessToken = accessToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

}
