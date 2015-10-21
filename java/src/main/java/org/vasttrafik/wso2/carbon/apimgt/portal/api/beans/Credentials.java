package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Credentials {

	private String userName;
	private String credential;

	public Credentials() {
	}

	public Credentials(String userName, String credential) {
		this.userName = userName;
		this.credential = credential;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

}
