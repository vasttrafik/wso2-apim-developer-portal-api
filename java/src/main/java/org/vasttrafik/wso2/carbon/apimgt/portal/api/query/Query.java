package org.vasttrafik.wso2.carbon.apimgt.portal.api.query;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

import javax.ws.rs.BadRequestException;
import java.util.Map;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Query implements ResourceBundleAware {

    private String attribute;
    private String trimmedValue;

    public Query(final String query, final String defaultAttribute) {
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
    }

    public boolean matches(final Map<String, String> map) {
        if (attribute == null) {
            return true;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (attribute.equalsIgnoreCase(entry.getKey())) {
                return trimmedValue.equalsIgnoreCase(entry.getValue());
            }
        }
        throw new BadRequestException(ResponseUtils.badRequest(resourceBundle, 2000L, new Object[][]{{attribute}}));
    }

}
