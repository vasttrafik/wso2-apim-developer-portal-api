package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.ApplicationDTO;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.SubscriptionDTO;

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

    public static Application valueOf(final ApplicationDTO applicationDTO, final SubscriptionDTO subscriptionDTO) {
        final Application application = new Application();
        application.id = applicationDTO.id;
        application.name = applicationDTO.name;
        application.throttlingTier = applicationDTO.tier;
        application.callbackUrl = applicationDTO.callbackUrl;
        application.status = applicationDTO.status;
        application.description = applicationDTO.description;
        if (subscriptionDTO != null) {
            application.consumerKey = subscriptionDTO.prodConsumerKey;
            application.consumerSecret = subscriptionDTO.prodConsumerSecret;
            application.accessToken = subscriptionDTO.prodKey;
        }
        return application;
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

    public boolean matches(String query) {
        final Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("name", name);
        map.put("status", status);
        map.put("description", description);
        return new Query(query, "name").matches(map);
    }

}
