package org.vasttrafik.wso2.carbon.apimgt.portal.api;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.API;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.pagination.PaginatedList;
import org.vasttrafik.wso2.carbon.apimgt.store.api.clients.ProxyClient;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;
import org.vasttrafik.wso2.carbon.identity.api.utils.ClientUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("apis")
public class APIs {

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
            final List<API> list = getProxyClient(authorization).getAPIs(query);
            return new PaginatedList<>(this.getClass(), offset, limit, query, list);
        } catch (final NotAuthorizedException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(exception);
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
        ResponseUtils.checkParameter(null, "apiId", true, new String[]{}, apiId);

        try {
            return getProxyClient(authorization).getAPI(apiId);
        } catch (final NotAuthorizedException | NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(exception);
        }
    }

}
