package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import java.util.List;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Application {

	private Integer id;
	private String name;
	private String description;
	private String status;
	private String throttlingTier;
	private String callbackUrl;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private List<Subscription> subscriptions;

	public Application() {
	}

	public Application(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getThrottlingTier() {
		return throttlingTier;
	}

	public void setThrottlingTier(String throttlingTier) {
		this.throttlingTier = throttlingTier;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

}
