package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.API;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Document;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.pagination.PaginatedList;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.clients.ProxyClient;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;
import org.vasttrafik.wso2.carbon.identity.api.utils.ClientUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("apis")
public class APIs implements ResourceBundleAware {

    static ProxyClient getProxyClient(final String authorization) throws Exception {
        if (authorization == null) {
            return new ProxyClient(ClientUtils.ADMIN_USER_NAME, ClientUtils.ADMIN_PASSWORD);
        } else {
            final String userName = Security.validateToken(authorization);
            return Security.getClient(userName);
        }
    }

    @GET
    public PaginatedList<API> getAPIs(
            @QueryParam("offset") @DefaultValue("0") final int offset,
            @QueryParam("limit") @DefaultValue("10") final int limit,
            @QueryParam("query") final String query,
            @HeaderParam("Authorization") final String authorization,
            @HeaderParam("If-None-Match") final String ifNoneMatch
    ) {
        try {
            final String DOCUMENT = "document:";
            final String documentQuery = query != null && query.startsWith(DOCUMENT) ? query.substring(DOCUMENT.length()).trim() : null;

            final ProxyClient client = getProxyClient(authorization);
            List<API> list = client.getAPIs(documentQuery != null ? null : query);

            // "Document is matched against all properties of all documents for all APIs."
            if (documentQuery != null) {
                final List<API> filteredList = new ArrayList<>();
                // Get all documents for the given api
                for (final API api : list) {
                    for (final Document document : client.getDocuments(api, null)) {
                        // Match against all searchable properties in the given document
                        if (document.matchesAny(documentQuery)) {
                            filteredList.add(api);
                            break;
                        }
                    }
                }
                list = filteredList;
            }

            return new PaginatedList<>(this.getClass(), offset, limit, query, list);
        } catch (final NotAuthorizedException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @GET
    @Path("{apiId}")
    public API getAPI(
            @PathParam("apiId") final String apiId,
            @HeaderParam("Authorization") final String authorization,
            @HeaderParam("If-None-Match") final String ifNoneMatch,
            @HeaderParam("If-Modified-Since") final String ifModifiedSince
    ) {
        ResponseUtils.checkParameter(resourceBundle, "apiId", true, new String[]{}, apiId);

        try {
            return getProxyClient(authorization).getAPI(apiId);
        } catch (final NotAuthorizedException | NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

}