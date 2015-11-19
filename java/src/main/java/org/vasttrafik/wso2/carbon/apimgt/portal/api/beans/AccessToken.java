package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class AccessToken {

    private String token;
    private String refreshToken;
    @JsonIgnore
    private Long created;
    @JsonIgnore
    private Long validityTime;

    public AccessToken(String token, String refreshToken, String validityTime) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.created = System.currentTimeMillis();
        this.validityTime = TimeUnit.SECONDS.toMillis(Long.valueOf(validityTime));
    }

    public String getToken() {
        return token;
    }

    @JsonProperty("expiresIn")
    public long calculateExpiresIn() {
        final Long now = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(created + validityTime - now);
    }
}
