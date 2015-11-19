package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.APIDTO;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

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
        if (query == null) {
            return true;
        } else {
            return matches(new Query(query, "name"));
        }
    }

    private boolean matches(Query query) {
        switch (query.getAttribute()) {
            case "id":
                return query.getTrimmedValue().equalsIgnoreCase(getId());
            case "name":
                return query.getTrimmedValue().equalsIgnoreCase(name);
            case "version":
                return query.getTrimmedValue().equalsIgnoreCase(version);
            case "context":
                return query.getTrimmedValue().equalsIgnoreCase(context);
            case "status":
                return query.getTrimmedValue().equalsIgnoreCase(status);
            case "description":
                return query.getTrimmedValue().equalsIgnoreCase(description);
            case "provider":
                return query.getTrimmedValue().equalsIgnoreCase(provider);
            case "document":
                throw new ServerErrorException(Response.Status.NOT_IMPLEMENTED); /// TODO: implement document
            default:
                throw new BadRequestException("Unknown attribute: " + query.getAttribute());
        }
    }

}
