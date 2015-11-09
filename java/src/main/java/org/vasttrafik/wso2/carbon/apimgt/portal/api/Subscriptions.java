package org.vasttrafik.wso2.carbon.apimgt.portal.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Subscription;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.InMemory;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.Persistence;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("subscriptions")
public class Subscriptions {

	private static final Persistence<Integer, Subscription> SUBSCRIPTIONS = new InMemory<Integer, Subscription>();

	static {
		for (int i = 1; i <= 6; i++) {
			SUBSCRIPTIONS.create(i, new Subscription(i));
		}
	}

	// GET /subscriptions | Accept, If-None-Match | 200, 304, 401, 406
	@GET
	public Response getSubscriptions(
			@QueryParam("limit") final Integer limit,
			@QueryParam("offset") final Integer offset,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch
	) {
		return Response.ok(SUBSCRIPTIONS.list()).build();
	}

	// POST /subscriptions | BODY | Content-Type | 201, 400, 401
	@POST
	public Response postSubscription(
			final Subscription subscription,
			@HeaderParam("Content-Type") final String contentType
	) {
		SUBSCRIPTIONS.create(subscription.getId(), subscription);
		return Response.status(201).entity(subscription).build();
	}

	// DELETE /subscriptions/{subscriptionId} | If-Match, If-Unmodified-Since | 200, 401, 404, 412
	@DELETE
	@Path("{subscriptionId}")
	public Response deleteSubscriptions(
			@PathParam("subscriptionId") final Integer subscriptionId,
			@HeaderParam("If-None-Match") final String ifNoneMatch,
			@HeaderParam("If-Unmodified-Since") final String ifUnmodifiedSince
	) {
		SUBSCRIPTIONS.delete(subscriptionId);
		return Response.ok().build();
	}

	// GET /subscriptions/{subscriptionId} | Accept, If-None-Match, If-Modified-Since | 200, 304, 401, 404, 406
	@GET
	@Path("{subscriptionId}")
	public Response getSubscription(
			@PathParam("subscriptionId") final Integer subscriptionId,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch,
			@HeaderParam("If-Modified-Since") final String ifModifiedSince
	) {
		return Response.ok(SUBSCRIPTIONS.read(subscriptionId)).build();
	}

	// PUT /subscriptions/{subscriptionId} | BODY | Content-Type, If-Match, If-Unmodified-Since | 200 | 400 | 401 | 404 | 412
	@PUT
	@Path("{subscriptionId}")
	public Response putSubscription(
			@PathParam("subscriptionId") final Integer subscriptionId,
			final Subscription subscription,
			@HeaderParam("Content-Type") final String contentType,
			@HeaderParam("If-Match") final String ifMatch,
			@HeaderParam("If-Unmodified-Since") final String ifUnmodifiedSince
	) {
		SUBSCRIPTIONS.update(subscriptionId, subscription);
		return Response.ok(subscription).build();
	}

	// PUT /subscriptions/{subscriptionId}/block-subscription | 200, 400, 401, 404
	@PUT
	@Path("{subscriptionId}/block-subscription")
	public Response putSubscriptionBlockSubscription(
			@PathParam("subscriptionId") final Integer subscriptionId
	) {
		return Response.ok(subscriptionId).build();
	}

}
