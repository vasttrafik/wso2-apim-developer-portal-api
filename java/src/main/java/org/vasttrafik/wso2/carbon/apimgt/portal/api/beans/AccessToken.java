package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class AccessToken {

	private String token;
	private String refreshToken;
	private Integer expiresIn;

	public AccessToken() {
	}

	public AccessToken(String token, String refreshToken, Integer expiresIn) {
		this.token = token;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

}
