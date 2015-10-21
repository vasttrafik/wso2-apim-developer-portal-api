package org.vasttrafik.wso2.carbon.apimgt.portal.api;

import javax.ws.rs.Consumes;
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
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.User;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.InMemory;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.Persistence;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("users")
public class Users {

	private static final Persistence<Integer, User> USERS = new InMemory<Integer, User>();

	static {
		for (int i = 1; i <= 6; i++) {
			final User user = new User("name " + i);
			user.setId(i);
			USERS.create(i, user);
		}
	}

	// POST /users | BODY | Content-Type | 201, 400, 401, 415
	@POST
	public Response postUser(
			final User user,
			@HeaderParam("Content-Type") final String contentType
	) {
		USERS.create(user.getId(), user); // XXX create id?
		return Response.status(Status.CREATED).entity(user).build();
	}

	// GET /users/{userId} | Accept, If-None-Match, If-Modified-Since | 200, 304, 401, 404, 406
	@GET
	@Path("{userId}")
	public Response getUser(
			@PathParam("userId") final Integer userId,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch,
			@HeaderParam("If-Modified-Since") final String ifModifiedSince
	) {
		return Response.ok(USERS.read(userId)).build();
	}

	// PUT /users/{userId} | action {updateProfile, updateCredential} | BODY | Content-Type, If-Match, If-Unmodified-Since | 200, 400, 401, 404, 412
	@PUT
	@Path("{userId}")
	public Response putUser(
			final User user,
			@PathParam("userId") final Integer userId,
			@QueryParam("action") final String action, // make this an enum?
			@HeaderParam("Content-Type") final String contentType,
			@HeaderParam("If-Match") final String ifMatch,
			@HeaderParam("If-Unmodified-Since") final String ifUnmodifiedSince
	) {
		USERS.update(userId, user);
		return Response.ok(user).build();
	}

}
