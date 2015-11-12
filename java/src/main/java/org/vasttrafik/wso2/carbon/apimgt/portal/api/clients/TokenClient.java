package org.vasttrafik.wso2.carbon.apimgt.portal.api.clients;

import org.glassfish.jersey.SslConfigurator;
import org.json.JSONObject;
import org.vasttrafik.wso2.carbon.identity.api.utils.ClientUtils;
import org.wso2.carbon.identity.oauth.stub.OAuthAdminServiceStub;
import org.wso2.carbon.identity.oauth.stub.dto.OAuthConsumerAppDTO;

import javax.net.ssl.SSLContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class TokenClient {

    private static final WebTarget SECURE_TARGET;

    static {
        final int port = 8243 + ClientUtils.PORT_OFFSET;
        final String baseUrl = String.format("https://%s:%d", ClientUtils.HOST_NAME, port);

        final SSLContext sslContext = SslConfigurator.newInstance()
                .trustStoreFile(ClientUtils.TRUST_STORE)
                .trustStorePassword(ClientUtils.TRUST_STORE_PASSWORD).createSSLContext();

        SECURE_TARGET = ClientBuilder.newBuilder().sslContext(sslContext).build().target(baseUrl);
    }

    private String token;
    private int expires;

    public void initialize(final String application) {
        try {
            // TODO: Update this code so that different key/secret is returned for different users
            final OAuthAdminServiceStub stub = ClientUtils.getOAuthAdminServiceStub();
            ClientUtils.authenticateIfNeeded(stub._getServiceClient());
            final OAuthConsumerAppDTO dto = stub.getOAuthApplicationDataByAppName(application);
            final String key = dto.getOauthConsumerKey();
            final String secret = dto.getOauthConsumerSecret();
            final byte[] bytes = String.format("%s:%s", key, secret).getBytes();
            final String authorizationToken = DatatypeConverter.printBase64Binary(bytes);

            final String jsonString = SECURE_TARGET.path("token")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Basic " + authorizationToken)
                    .post(Entity.form(new Form("grant_type", "client_credentials")), String.class);
            final JSONObject object = new JSONObject(jsonString);
            token = object.getString("access_token");
            expires = object.getInt("expires_in");
        } catch (final Exception exception) {
            throw new InternalServerErrorException(exception);
        }
    }

    public String getToken() {
        return token;
    }

    public int getExpires() {
        return expires;
    }
}
