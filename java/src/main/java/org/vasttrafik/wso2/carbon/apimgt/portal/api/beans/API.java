package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.URLCodec;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.APIDTO;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class API {

    private String name;
    private String description;
    private String context;
    private String version;
    private String provider;
    private String status;

    public static String getId(final String name, final String version, final String provider) {
        return URLCodec.encodeUTF8(String.format("%s--%s_%s", name, version, provider));
    }

    public static API valueOf(final APIDTO apidto) {
        final API api = new API();
        api.name = apidto.name;
        api.version = apidto.version;
        api.provider = apidto.provider;
        api.context = apidto.context;
        api.status = apidto.status;
        api.description = apidto.description;
        return api;
    }

    @JsonProperty("id")
    public String getId() {
        return getId(name, version, provider);
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return UriBuilder.fromPath("apis/{apiId}/image").build(getId()).toString();
    }

    @JsonProperty("swagger")
    public String getSwaggerUrl() {
        return UriBuilder.fromPath("apis/{apiId}/swagger").build(getId()).toString();
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getProvider() {
        return provider;
    }

    public boolean matches(String query) {
        final Map<String, String> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", name);
        map.put("version", version);
        map.put("context", context);
        map.put("status", status);
        map.put("provider", provider);
        map.put("description", description);
        return new Query(query, "name").matches(map);
    }

}
