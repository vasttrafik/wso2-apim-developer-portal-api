package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Authorization;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Status;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Application;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.clients.ProxyClient;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Authorization
@Produces(MediaType.APPLICATION_JSON)
@Path("applications/{applicationId}/tokens")
public final class Tokens implements ResourceBundleAware {

    @Context
    private SecurityContext securityContext;

    @Status(Status.CREATED)
    @POST
    public Application postApplicationTokens(
            @PathParam("applicationId") final Integer applicationId,
            @QueryParam("validityTime") final Integer validityTime
    ) {
        ResponseUtils.checkParameter(resourceBundle, "applicationId", true, new String[]{}, String.valueOf(applicationId));
        ResponseUtils.checkParameter(resourceBundle, "validityTime", true, new String[]{}, String.valueOf(validityTime));

        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            return client.generateApplicationToken(applicationId, String.valueOf(validityTime));
        } catch (final NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

}
