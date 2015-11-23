package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Authorization;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Subscription;
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
@Path("subscriptions")
public class Subscriptions implements ResourceBundleAware {

    @Context
    private SecurityContext securityContext;

    @GET
    public PaginatedList<Subscription> getSubscriptions(
            @QueryParam("offset") @DefaultValue("0") final int offset,
            @QueryParam("limit") @DefaultValue("10") final int limit,
            @HeaderParam("If-None-Match") final String ifNoneMatch
    ) {
        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            final List<Subscription> list = client.getSubscriptions();
            return new PaginatedList<>(this.getClass(), offset, limit, null, list);
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @GET
    @Path("{subscriptionId}")
    public Subscription getSubscription(
            @PathParam("subscriptionId") final Integer subscriptionId,
            @HeaderParam("If-None-Match") final String ifNoneMatch,
            @HeaderParam("If-Modified-Since") final String ifModifiedSince
    ) {
        ResponseUtils.checkParameter(resourceBundle, "subscriptionId", true, new String[]{}, String.valueOf(subscriptionId));

        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            return client.getSubscription(subscriptionId);
        } catch (final NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @POST
    public Subscription postSubscription(
            final Subscription subscription
    ) {
        ResponseUtils.checkParameter(resourceBundle, "apiName", true, new String[]{}, subscription.getApi().getName());
        ResponseUtils.checkParameter(resourceBundle, "apiVersion", true, new String[]{}, subscription.getApi().getVersion());
        ResponseUtils.checkParameter(resourceBundle, "apiProvider", true, new String[]{}, subscription.getApi().getProvider());
        ResponseUtils.checkParameter(resourceBundle, "applicationName", true, new String[]{}, subscription.getApplication().getName());
        ResponseUtils.checkParameter(resourceBundle, "applicationTier", true, new String[]{}, subscription.getApplication().getThrottlingTier());

        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            return client.addSubscription(subscription);
        } catch (final BadRequestException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

    @DELETE
    @Path("{subscriptionId}")
    public void deleteSubscriptions(
            @PathParam("subscriptionId") final Integer subscriptionId,
            @HeaderParam("If-None-Match") final String ifNoneMatch,
            @HeaderParam("If-Unmodified-Since") final String ifUnmodifiedSince
    ) {
        ResponseUtils.checkParameter(resourceBundle, "subscriptionId", true, new String[]{}, String.valueOf(subscriptionId));

        try {
            final ProxyClient client = Security.getClient(securityContext.getUserPrincipal().getName());
            final Subscription subscription = client.getSubscription(subscriptionId);
            client.removeSubscription(subscription);
        } catch (final BadRequestException | NotFoundException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }

}
