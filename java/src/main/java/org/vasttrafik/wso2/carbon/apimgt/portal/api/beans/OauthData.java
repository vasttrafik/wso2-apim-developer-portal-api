package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.SubscriptionDTO;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class OauthData {

    private String token;
    private String key;
    private String secret;
    private String validityTime;

    public static OauthData valueOf(final SubscriptionDTO subscriptionDTO) {
        final OauthData oauthData = new OauthData();
        oauthData.token = subscriptionDTO.prodKey;
        oauthData.key = subscriptionDTO.prodConsumerKey;
        oauthData.secret = subscriptionDTO.prodConsumerSecret;
        oauthData.validityTime = String.valueOf(subscriptionDTO.prodValidityTime);
        return oauthData;
    }

    public OauthData setToken(final String token) {
        this.token = token;
        return this;
    }

    public OauthData setValidityTime(final String validityTime) {
        this.validityTime = validityTime;
        return this;
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }

    public String getToken() {
        return token;
    }

    public String getValidityTime() {
        return validityTime;
    }

}
