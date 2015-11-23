package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Authorization;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Application;
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
@Authorization
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("applications")
public class Applications implements ResourceBundleAware {

    @Context
    private SecurityContext securityContext;

    @GET
    public PaginatedList<Application> getApplications(
            @QueryParam("offset") @DefaultValue("0") final int offset,
            @QueryParam("limit") @DefaultValue("10") final int limit,
            @QueryParam("query") final String query,
            @HeaderParam("If-None-Match") final String ifNoneMatch
    ) {
        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            final List<Application> list = client.getApplications(query);
            return new PaginatedList<>(this.getClass(), offset, limit, query, list);
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @GET
    @Path("{applicationId}")
    public Application getApplication(
            @PathParam("applicationId") final Integer applicationId,
            @HeaderParam("If-None-Match") final String ifNoneMatch,
            @HeaderParam("If-Modified-Since") final String ifModifiedSince
    ) {
        ResponseUtils.checkParameter(resourceBundle, "applicationId", true, new String[]{}, String.valueOf(applicationId));

        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            return client.getApplication(applicationId);
        } catch (final NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @POST
    public Application postApplications(
            final Application application
    ) {
        ResponseUtils.checkParameter(resourceBundle, "applicationName", true, new String[]{}, application.getName());
        ResponseUtils.checkParameter(null, "applicationTier", true, new String[]{}, application.getThrottlingTier());
        ResponseUtils.checkParameter(null, "applicationDescription", true, new String[]{}, application.getDescription());
        ResponseUtils.checkParameter(null, "applicationCallbackUrl", true, new String[]{}, application.getCallbackUrl());

        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            return client.addApplication(application);
        } catch (final BadRequestException | NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @PUT
    @Path("{applicationId}")
    public Application putApplication(
            @PathParam("applicationId") final Integer applicationId,
            final Application application,
            @HeaderParam("If-Match") final String ifMatch,
            @HeaderParam("If-Unmodified-Since") final String ifUnmodifiedSince
    ) {
        ResponseUtils.checkParameter(null, "applicationId", true, new String[]{}, String.valueOf(application));
        ResponseUtils.checkParameter(null, "applicationName", true, new String[]{}, application.getName());
        ResponseUtils.checkParameter(null, "applicationTier", true, new String[]{}, application.getThrottlingTier());
        ResponseUtils.checkParameter(null, "applicationDescription", true, new String[]{}, application.getDescription());
        ResponseUtils.checkParameter(null, "applicationCallbackUrl", true, new String[]{}, application.getCallbackUrl());

        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            return client.updateApplication(applicationId, application);
        } catch (final BadRequestException | NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @DELETE
    @Path("{applicationId}")
    public void deleteApplication(
            @PathParam("applicationId") final Integer applicationId,
            @HeaderParam("If-Match") final String ifMatch,
            @HeaderParam("If-Unmodified-Since") final String ifUnmodifiedSince
    ) {
        ResponseUtils.checkParameter(resourceBundle, "applicationId", true, new String[]{}, String.valueOf(applicationId));

        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            client.removeApplication(applicationId);
        } catch (final NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

}
