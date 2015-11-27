package org.vasttrafik.wso2.carbon.apimgt.store.api.beans;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class DocumentDTO {

    public static enum SourceType {
        FILE, INLINE, URL
    }

    public String name;
    public SourceType sourceType;
    public String summary;
    public String content;
    public String sourceUrl;
    public String filePath;
    public String type;
    public String otherTypeName;

}
