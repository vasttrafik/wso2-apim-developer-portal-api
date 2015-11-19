package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.DocumentDTO;

import javax.ws.rs.BadRequestException;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Document {

    public enum Type {
        file, inline, url
    }

    public Integer id;
    public String name;
    public Type type;
    public String summary;
    public String content;
    public String url;

    public Document(final DocumentDTO dto) {

        id = dto.name.hashCode(); // TODO: Documents have ID in the Swagger documentation;
        name = dto.name;
        type = Type.valueOf(dto.sourceType.name().toLowerCase());
        summary = dto.summary;
        content = null;
        url = dto.sourceUrl;

    }

    public Integer getId() {
        return id;
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
                return query.getTrimmedValue().equalsIgnoreCase(String.valueOf(id));
            case "name":
                return query.getTrimmedValue().equalsIgnoreCase(name);
            case "type":
                return query.getTrimmedValue().equalsIgnoreCase(type.name());
            case "summary":
                return query.getTrimmedValue().equalsIgnoreCase(summary);
            default:
                throw new BadRequestException("Unknown attribute: " + query.getAttribute());
        }
    }

}
