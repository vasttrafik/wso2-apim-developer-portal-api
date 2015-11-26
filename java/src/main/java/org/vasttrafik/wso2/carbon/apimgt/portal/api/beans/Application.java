package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
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

    public Application setSubscriptions(final List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }

    public Application setCallbackUrl(final String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }

    public Application setDescription(final String description) {
        this.description = description;
        return this;
    }

    public Application setId(final Integer id) {
        this.id = id;
        return this;
    }

    public Application setThrottlingTier(final String throttlingTier) {
        this.throttlingTier = throttlingTier;
        return this;
    }

    public Application setName(final String name) {
        this.name = name;
        return this;
    }

    public Application setStatus(final String status) {
        this.status = status;
        return this;
    }

    public Application setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Application setConsumerKey(final String consumerKey) {
        this.consumerKey = consumerKey;
        return this;
    }

    public Application setConsumerSecret(final String consumerSecret) {
        this.consumerSecret = consumerSecret;
        return this;
    }

    public boolean hasAccessToken() {
        return accessToken != null;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThrottlingTier() {
        return throttlingTier;
    }

    public String getDescription() {
        return description;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public boolean matches(String query) {
        final Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("name", name);
        map.put("status", status);
        map.put("description", description);
        return new Query(query, "name").matches(map);
    }

}
