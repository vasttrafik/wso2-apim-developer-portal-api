package org.vasttrafik.wso2.carbon.apimgt.store.api.beans;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class RefreshTokenDTO extends WrapperDTO {

    public static class Key {
        public String accessToken;
        public String consumerKey;
        public String consumerSecret;
        public String validityTime;
        public Boolean enableRegenarate;
        
        // New values
        public String tokenDetails;
        public String appDetails;
        public Object tokenScope; // Array with scope Strings
        public Object responseParams;
    }

    public static class Data {
        public Key key;
    }

    public Data data;

}
