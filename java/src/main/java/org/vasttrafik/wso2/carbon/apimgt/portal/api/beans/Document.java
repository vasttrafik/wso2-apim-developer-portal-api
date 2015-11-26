package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.query.Query;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.URLCodec;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.DocumentDTO;
import org.vasttrafik.wso2.carbon.common.api.utils.RegistryUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Document {

    public enum Source {
        file, inline, url
    }

    public static class Type {
        private String type;
        private String otherType;

        private Type(final String type, final String otherType) {
            this.type = type;
            this.otherType = otherType;
        }
    }

    private String name;
    private Source source;
    private Type type;
    private String summary;
    private String url;
    private String fileName;

    @JsonIgnore
    private String content;
    @JsonIgnore
    private String filePath;

    public static Document valueOf(final DocumentDTO documentDTO) {
        final Document document = new Document();
        document.name = documentDTO.name;
        document.source = Source.valueOf(documentDTO.sourceType.name().toLowerCase());
        document.type = new Type(documentDTO.type, documentDTO.otherTypeName);
        document.summary = documentDTO.summary;
        document.url = documentDTO.sourceUrl;
        document.content = documentDTO.content;
        document.filePath = documentDTO.filePath;
        document.fileName = URLCodec.encodeUTF8(StringUtils.substringAfterLast(document.filePath, "/"));
        return document;
    }

    @JsonProperty("id")
    public String getId() {
        return URLCodec.encodeUTF8(name);
    }

    public Object getContent() {
        if (Source.file.equals(source) && filePath != null) {
            try {
                return RegistryUtils.getContent(filePath.substring("/registry/resource".length()));
            } catch (final Exception exception) {
            }
        }
        return content;
    }

    public boolean matchesAny(String query) {
        for (final String value : getMap().values()) {
            if (query.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean matches(String query) {
        return new Query(query, "name").matches(getMap());
    }

    private Map<String, String> getMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", name);
        map.put("type", source.name());
        map.put("summary", summary);
        return map;
    }

}
