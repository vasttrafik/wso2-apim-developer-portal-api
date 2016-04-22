package org.vasttrafik.wso2.carbon.apimgt.store.api.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    public Boolean prodRegenerateOption;
    public Boolean prodRegenarateOption;
    public String prodKeyState;
    public String prodAuthorizedDomains;
    public Integer prodValidityTime;
    public String sandboxKey;
    public String sandboxConsumerKey;
    public String sandboxConsumerSecret;
    public Boolean sandRegenerateOption;
    public Boolean sandRegenarateOption;
    public String sandboxKeyState;
    public String sandboxAuthorizedDomains;
    public Integer sandValidityTime;
    public List<SubscriptionsItemDTO> subscriptions;
    // Nya attribut
    public String prodJsonString;
    public String prodKeyScope;
    public String prodKeyScopeValue;
    public String sandKeyScope;
    public String sandKeyScopeValue;
    public String sandboxJsonString;
    @JsonIgnore
    public Object scopes;
}
