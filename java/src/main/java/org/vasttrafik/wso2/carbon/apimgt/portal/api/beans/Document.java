package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.DocumentDTO;

import java.util.HashMap;
import java.util.Map;

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
        final Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("name", name);
        map.put("type", type.name());
        map.put("summary", summary);
        return new Query(query, "name").matches(map);
    }

}
