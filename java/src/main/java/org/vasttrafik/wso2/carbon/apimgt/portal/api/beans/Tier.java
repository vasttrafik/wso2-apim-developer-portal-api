package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Tier {

	private String name;
	private String rate;
	private String roles;

	public Tier() {
	}

	public Tier(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

}
