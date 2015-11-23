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

    public static AccessToken valueOf(final String token, final String refreshToken, final String validityTime) {
        final AccessToken accessToken = new AccessToken();
        accessToken.token = token;
        accessToken.refreshToken = refreshToken;
        accessToken.created = System.currentTimeMillis();
        accessToken.validityTime = TimeUnit.SECONDS.toMillis(Long.valueOf(validityTime));
        return accessToken;
    }

    @JsonProperty("expiresIn")
    public long calculateExpiresIn() {
        final Long now = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(created + validityTime - now);
    }
}
