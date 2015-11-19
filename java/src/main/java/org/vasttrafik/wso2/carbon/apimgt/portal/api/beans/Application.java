package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.ApplicationDTO;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.SubscriptionDTO;

import javax.ws.rs.BadRequestException;
import java.util.List;

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

    public Application() {}

    public Application(final ApplicationDTO applicationDTO, final SubscriptionDTO subscriptionDTO) {
        this.id = applicationDTO.id;
        this.name = applicationDTO.name;
        this.throttlingTier = applicationDTO.tier;
        this.callbackUrl = applicationDTO.callbackUrl;
        this.status = applicationDTO.status;
        this.description = applicationDTO.description;
        if (subscriptionDTO != null) {
            this.consumerKey = subscriptionDTO.prodConsumerKey;
            this.consumerSecret = subscriptionDTO.prodConsumerSecret;
            this.accessToken = subscriptionDTO.prodKey;
        }
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

    public boolean matches(String query) {
        if (query == null) {
            return true;
        } else {
            return matches(new Query(query, "name"));
        }
    }

    private boolean matches(Query query) {
        switch (query.getAttribute()) {
            case "id":
                return query.getTrimmedValue().equalsIgnoreCase(String.valueOf(id));
            case "name":
                return query.getTrimmedValue().equalsIgnoreCase(name);
            // TOOD: Requirements says to implement tier.
            //case "tier":
            //	return query.getTrimmedValue().equalsIgnoreCase(tier);
            case "status":
                return query.getTrimmedValue().equalsIgnoreCase(status);
            case "description":
                return query.getTrimmedValue().equalsIgnoreCase(description);
            default:
                throw new BadRequestException("Unknown attribute: " + query.getAttribute());
        }
    }

    public String getName() {
        return name;
    }

    public String getThrottlingTier() {
        return throttlingTier;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    String getStatus() {
        return status;
    }
}
