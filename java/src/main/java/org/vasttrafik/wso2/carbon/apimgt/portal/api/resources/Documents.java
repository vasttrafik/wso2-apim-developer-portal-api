/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.API;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Document;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.pagination.PaginatedList;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.clients.ProxyClient;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("apis/{apiId}/documents")
public class Documents implements ResourceBundleAware {

    @Context
    private SecurityContext securityContext;

    @GET
    public PaginatedList<Document> getDocuments(
            @PathParam("apiId") final String apiId,
            @QueryParam("offset") @DefaultValue("0") final int offset,
            @QueryParam("limit") @DefaultValue("10") final int limit,
            @QueryParam("query") final String query,
            @HeaderParam("Authorization") final String authorization,
            @HeaderParam("If-None-Match") final String ifNoneMatch
    ) {
        ResponseUtils.checkParameter(resourceBundle, "apiId", true, new String[]{}, apiId);

        try {
            final ProxyClient client = APIs.getProxyClient(authorization);
            final API api = client.getAPI(apiId);
            final List<Document> list = client.getDocuments(api, query);
            return new PaginatedList<>(this.getClass(), offset, limit, query, list, apiId);
        } catch (final BadRequestException | NotAuthorizedException | NotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @GET
    @Path("{documentId}")
    public Document getDocument(
            @PathParam("apiId") final String apiId,
            @PathParam("documentId") final String documentId,
            @HeaderParam("Authorization") final String authorization,
            @HeaderParam("If-None-Match") final String ifNoneMatch,
            @HeaderParam("If-Modified-Since") final String ifModifiedSince
    ) {
        ResponseUtils.checkParameter(resourceBundle, "apiId", true, new String[]{}, apiId);
        ResponseUtils.checkParameter(resourceBundle, "documentId", true, new String[]{}, documentId);

        try {
            final ProxyClient client = APIs.getProxyClient(authorization);
            final API api = client.getAPI(apiId);
            return client.getDocument(api, documentId);
        } catch (final BadRequestException | NotAuthorizedException | NotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }
}
