package org.vasttrafik.wso2.carbon.apimgt.store.api.beans;

import java.util.List;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class SubscriptionDTO {

    public Integer id;
    public String name;
    public String callbackUrl;
    public String prodKey;
    public String prodConsumerKey;
    public String prodConsumerSecret;
    public String prodRegenarateOption;
    public String prodAuthorizedDomains;
    public Integer prodValidityTime;
    public String sandboxKey;
    public String sandboxConsumerKey;
    public String sandboxConsumerSecret;
    public String sandRegenarateOption;
    public String sandboxAuthorizedDomains;
    public Integer sandValidityTime;
    public List<SubscriptionsItemDTO> subscriptions;

}
