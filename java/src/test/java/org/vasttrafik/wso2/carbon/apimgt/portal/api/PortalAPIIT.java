package org.vasttrafik.wso2.carbon.apimgt.portal.api;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

public class PortalAPIIT {

	private static WebTarget target;

	@BeforeClass
	public static void beforeClass() {
		final String endpointUrl = System.getProperty("service.url");
		target = ClientBuilder.newClient().target(endpointUrl);
	}

	@Test
	public void testGetTier() throws Exception {
		final Response reponse = target.path("/api/tiers").request().accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), reponse.getStatus());
	}

}
