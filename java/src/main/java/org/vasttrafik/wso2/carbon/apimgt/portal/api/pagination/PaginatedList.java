package org.vasttrafik.wso2.carbon.apimgt.portal.api.pagination;

import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class PaginatedList<T> {

    private final String count;
    private final List<T> list;

    private String previous;
    private String next;

    public PaginatedList(final Class<?> resource, int offset, int limit, final String query, final List<T> unpaginatedList, final Object... objects) {
        offset = offset < 0 ? 0 : offset < unpaginatedList.size() ? offset : unpaginatedList.size();
        limit = limit >= 0 && limit <= unpaginatedList.size() ? limit : unpaginatedList.size();

        final int fromIndex = offset;
        final int toIndex = offset + limit < unpaginatedList.size() ? offset + limit : unpaginatedList.size();
        list = unpaginatedList.subList(fromIndex, toIndex);
        count = "" + unpaginatedList.size();

        final int nextOffset = offset + limit;
        if (nextOffset < unpaginatedList.size()) {
            next = buildUrl(resource, nextOffset, limit, query, objects);
        }

        if (offset > 0) {
            final int previousOffset = offset - limit;
            previous = buildUrl(resource, previousOffset > 0 ? previousOffset : 0, limit, query, objects);
        }
    }

    private String buildUrl(final Class<?> resource, final int offset, final int limit, final String query, final Object... objects) {
        UriBuilder builder = UriBuilder.fromResource(resource)
                .queryParam("offset", offset)
                .queryParam("limit", limit);
        if (query != null) {
            builder.queryParam("query", query);
        }
        return builder.build(objects).toString();
    }

}
