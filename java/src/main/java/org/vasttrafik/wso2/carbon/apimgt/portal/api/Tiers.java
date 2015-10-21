package org.vasttrafik.wso2.carbon.apimgt.portal.api;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Tier;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.InMemory;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.Persistence;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("tiers")
public class Tiers {

	private static final Persistence<Integer, Tier> TIERS = new InMemory<Integer, Tier>();

	static {
		for (int i = 1; i <= 6; i++) {
			TIERS.create(i, new Tier("name " + i));
		}
	}

	// GET /tiers | Accept, If-None-Match | 200, 304, 401, 406
	@GET
	public Response getTiers(
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch
	) {
		return Response.ok(TIERS.list()).build();
	}
}
