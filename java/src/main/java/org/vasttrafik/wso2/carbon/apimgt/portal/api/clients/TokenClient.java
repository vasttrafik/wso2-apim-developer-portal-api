package org.vasttrafik.wso2.carbon.apimgt.portal.api.clients;

import org.vasttrafik.wso2.carbon.identity.api.utils.ClientUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class TokenClient {

    private static final WebTarget TARGET;

    static {
        final int port = 8280 + ClientUtils.PORT_OFFSET;
        final String baseUrl = "http://" + ClientUtils.HOST_NAME + ":" + port + "/revoke";
        TARGET = ClientBuilder.newClient().target(baseUrl);
    }

    public void revoke(final String accessToken, final String key, final String secret) {
        final byte[] bytes = String.format("%s:%s", key, secret).getBytes();
        final String base64 = DatatypeConverter.printBase64Binary(bytes);
        final String authorization = String.format("Basic %s", base64);

        final Form form = new Form("token", accessToken);
        TARGET.request().header("Authorization", authorization).post(Entity.form(form));
    }

}
