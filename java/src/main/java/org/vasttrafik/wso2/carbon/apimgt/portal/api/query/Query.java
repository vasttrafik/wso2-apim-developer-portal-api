package org.vasttrafik.wso2.carbon.apimgt.portal.api.query;

import javax.ws.rs.BadRequestException;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Query {

    private final String query;
    private String attribute;
    private String trimmedValue;

    public Query(final String query, final String defaultAttribute) {
        try {
            this.query = query;
            if (query != null) {
                if (!query.contains(":")) {
                    attribute = defaultAttribute;
                    trimmedValue = query.trim();
                } else {
                    String[] strings = query.split(":", 2);
                    attribute = strings[0];
                    trimmedValue = strings[1].trim();
                }
            }
        } catch (final Exception exception) {
            throw new BadRequestException(exception);
        }
    }

    public boolean isNull() {
        return query == null;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getTrimmedValue() {
        return trimmedValue;
    }

}
