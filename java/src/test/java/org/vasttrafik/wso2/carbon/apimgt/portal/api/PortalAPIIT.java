package org.vasttrafik.wso2.carbon.apimgt.portal.api;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.WebClient;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Application;

public class PortalAPIIT {

	private static String endpointUrl;

	@BeforeClass
	public static void beforeClass() {
		endpointUrl = System.getProperty("service.url");
	}

	@Test
	public void testGetTier() throws Exception {
		final WebClient client = WebClient.create(endpointUrl + "/api/tiers");
		final Response reponse = client.accept("application/json").get();
		assertEquals(Response.Status.OK.getStatusCode(), reponse.getStatus());
	}

	@Test
	public void testPostApplication() throws Exception {
		final List<Object> providers = new ArrayList<Object>();
		providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
		final WebClient client = WebClient.create(endpointUrl + "/api/applications", providers);
		final Application application = new Application(1234);
		final Response response = client.accept("application/json").type("application/json").post(application);
		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
	}

}
