package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.APIDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class API {

    public enum Transport {
        http, https
    }

    public API(final APIDTO dto) {
        name = dto.name;
        version = dto.version;
        provider = dto.provider;
        context = dto.context;
        status = dto.status;
        description = dto.description;
        imageUrl = dto.thumbnailurl;
    }

    public static String getId(final String name, final String version, final String provider) {
        return String.format("%s--%s_%s", name, version, provider);
    }

    @JsonProperty("id")
    public String getId() {
        return getId(name, version, provider);
    }

    private String name;
    private String description;
    private String imageUrl;
    private String context;
    private String version;
    private String provider;
    private String swagger;
    private String status;
    private String responseCaching;
    private Date lastModifiedDate;
    private Boolean isDefaultVersion;
    private List<Transport> transports;
    private String tier;
    private Endpoint endpoint;

    public API() {}

    public API(final String id) {
        String[] strings = id.split("--", 2);
        name = strings[0];
        strings = strings[1].split("_", 2);
        version = strings[0];
        provider = strings[1];
    }

    public API(final String name, final String version, final String provider) {
        this.name = name;
        this.version = version;
        this.provider = provider;
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
