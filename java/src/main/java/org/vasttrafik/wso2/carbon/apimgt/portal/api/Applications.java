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
import javax.ws.rs.core.Response.Status;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Application;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.InMemory;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.Persistence;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("applications")
public class Applications {

	private static final Persistence<Integer, Application> APPLICATIONS = new InMemory<Integer, Application>();

	static {
		for (int i = 1; i <= 6; i++) {
			APPLICATIONS.create(i, new Application(i));
		}
	}

	// GET /applications | limit, offset, query | Accept, If-None-Match | 200, 304, 400, 401, 406
	@GET
	public Response getApplications(
			@QueryParam("limit") final Integer limit,
			@QueryParam("offset") final Integer offset,
			@QueryParam("query") final String query,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch
	) {
		return Response.ok(APPLICATIONS.list()).build();
	}

	// POST /applications | BODY | Content-Type | 201, 400, 401, 415
	@POST
	public Response postApplications(
			final Application application,
			@QueryParam("limit") final Integer limit,
			@QueryParam("offset") final Integer offset,
			@QueryParam("query") final String query,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch
	) {
		APPLICATIONS.create(application.getId(), application);
		return Response.status(Status.CREATED).entity(APPLICATIONS.read(application.getId())).build();
	}

	// DELETE /applications/{applicationId} | If-Match, If-Unmodified-Since | 200, 201,
	@DELETE
	@Path("{applicationId}")
	public Response deleteApplication(
			@PathParam("applicationId") final Integer applicationId,
			@HeaderParam("If-Match") final String ifMatch,
			@HeaderParam("If-Unmodified-Since") final String ifUnmodifiedSince
	) {
		return Response.ok(APPLICATIONS.delete(applicationId)).build();
	}

	// GET /applications/{applicationId} | Accept, If-None-Match, If-Modified-Since | 200, 304, 401, 404, 406
	@GET
	@Path("{applicationId}")
	public Response getApplication(
			@PathParam("applicationId") final Integer applicationId,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch,
			@HeaderParam("If-Modified-Since") final String ifModifiedSince
	) {
		return Response.ok(APPLICATIONS.read(applicationId)).build();
	}

	// PUT /applications/{applicationId} | BODY | Content-Type, If-Match, If-Unmodified-Since | 200, 400, 401, 404, 412
	@PUT
	@Path("{applicationId}")
	public Response putApplication(
			@PathParam("applicationId") final Integer applicationId,
			final Application application,
			@HeaderParam("Content-Type") final String contentType,
			@HeaderParam("If-Match") final String ifMatch,
			@HeaderParam("If-Unmodified-Since") final String ifUnmodifiedSince
	) {
		APPLICATIONS.update(applicationId, application);
		return Response.ok(application).build();
	}

	// POST /applications/{applicationId}/tokens | validityTime | Content-Type | 201, 400, 401, 415
	@POST
	@Path("{applicationId}/tokens")
	public Response postApplicationTokens(
			@PathParam("applicationId") final Integer applicationId,
			@QueryParam("validityTime") final Integer validityTime,
			@HeaderParam("Content-Type") final String contentType
	) {
		return Response.status(Status.CREATED).entity(APPLICATIONS.read(applicationId)).header("Location", "LOCATION").build();
	}
}
