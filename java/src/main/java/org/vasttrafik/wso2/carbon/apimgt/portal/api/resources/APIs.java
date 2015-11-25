package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.API;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Document;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.pagination.PaginatedList;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.clients.ProxyClient;
import org.vasttrafik.wso2.carbon.common.api.utils.RegistryUtils;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;
import org.vasttrafik.wso2.carbon.identity.api.utils.ClientUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        } catch (final BadRequestException | NotAuthorizedException | NotFoundException exception) {
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
        } catch (final BadRequestException | NotAuthorizedException | NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @GET
    @Path("{apiId}/image")
    @Produces({"image/jpeg"})
    public Response getImage(
            @PathParam("apiId") final String apiId,
            @HeaderParam("Authorization") final String authorization
    ) {
        ResponseUtils.checkParameter(resourceBundle, "apiId", true, new String[]{}, apiId);

        try {
            final API api = getProxyClient(authorization).getAPI(apiId);
            final String pathFormat = "/_system/governance/apimgt/applicationdata/icons/%s/%s/%s/icon";
            final String path = String.format(pathFormat, api.getProvider(), api.getName(), api.getVersion());

            final Object content = RegistryUtils.getContent(path);
            return Response.ok(content).build();
        } catch (final BadRequestException | NotAuthorizedException | NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @GET
    @Path("{apiId}/swagger")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson(
            @PathParam("apiId") final String apiId,
            @HeaderParam("Authorization") final String authorization
    ) {
        ResponseUtils.checkParameter(resourceBundle, "apiId", true, new String[]{}, apiId);

        return getJson(apiId, "api-doc", authorization);
    }

    @GET
    @Path("{apiId}/swagger/{resourceName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson(
            @PathParam("apiId") final String apiId,
            @PathParam("resourceName") final String resourceName,
            @HeaderParam("Authorization") final String authorization
    ) {
        ResponseUtils.checkParameter(resourceBundle, "apiId", true, new String[]{}, apiId);
        ResponseUtils.checkParameter(resourceBundle, "resourceName", true, new String[]{}, resourceName);

        try {
            final API api = getProxyClient(authorization).getAPI(apiId);
            final String pathFormat = "/_system/governance/apimgt/applicationdata/api-docs/%s-%s-%s/1.2/%s";
            final String path = String.format(pathFormat, api.getName(), api.getVersion(), api.getProvider(), resourceName);

            final Object content = RegistryUtils.getContent(path);
            return Response.ok(content).build();
        } catch (final BadRequestException | NotAuthorizedException | NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

}
