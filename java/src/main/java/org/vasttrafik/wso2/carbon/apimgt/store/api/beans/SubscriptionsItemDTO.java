package org.vasttrafik.wso2.carbon.apimgt.store.api.beans;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class SubscriptionsItemDTO {

    public String name;
    public String provider;
    public String version;
    public String status;
    public String tier;
    public String subStatus;
    public String thumburl;
    public String context;
    public String prodKey;
    public String prodConsumerKey;
    public String prodConsumerSecret;
    public String prodAuthorizedDomains;
    public Integer prodValidityTime;
    public String sandboxKey;
    public String sandboxConsumerKey;
    public String sandboxConsumerSecret;
    public String sandAuthorizedDomains;
    public Integer sandValidityTime;
    public String hasMultipleEndpoints;
    // Nytt attribut
    public String businessOwner;
}
