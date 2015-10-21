package org.vasttrafik.wso2.carbon.apimgt.portal.api;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.API;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Document;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.InMemory;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence.Persistence;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Path("apis")
@Produces(MediaType.APPLICATION_JSON)
public class APIs {

	private static final Persistence<String, API> APIS = new InMemory<String, API>();
	private static final Map<String, Persistence<Integer, Document>> DOCUMENTS = new HashMap<String, Persistence<Integer, Document>>();

	static {
		for (String string : new String[]{"a", "b", "c", "d", "e"}) {
			final API api = new API(string, string, string);
			api.setProvider(string);
			APIS.create(api.getId(), api);

			final Persistence<Integer, Document> persistence = new InMemory<Integer, Document>();
			for (int i = 1; i <= 6; i++) {
				persistence.create(i, new Document(i));
			}
			DOCUMENTS.put(api.getId(), persistence);
		}
	}

	// GET /apis | limit, offset, query | Accept, If-None-Match | 200, 304, 401, 406
	@GET
	public Response getAPIs(
			@QueryParam("limit") final Integer limit,
			@QueryParam("offset") final Integer offset,
			@QueryParam("query") final String query,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch
	) {
		return Response.ok().entity(APIS.list()).build();
	}

	// GET /apis/{apiId} | Accept, If-None-Match, If-Modified-Since | 200, 304, 401, 404, 406
	@GET
	@Path("{apiId}")
	public Response getAPI(
			@PathParam("apiId") final String apiId,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch,
			@HeaderParam("If-Modified-Since") final String ifModifiedSince
	) {
		return Response.ok(APIS.read(apiId)).build();
	}

	// GET /apis/{apiId}/documents | limit, offset, query | Accept, If-None-Match | 200, 304, 401, 404, 406
	@GET
	@Path("{apiId}/documents")
	public Response getAPIDocuments(
			@PathParam("apiId") final String apiId,
			@QueryParam("limit") final Integer limit,
			@QueryParam("offset") final Integer offset,
			@QueryParam("query") final String query,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch
	) {
		return Response.ok(DOCUMENTS.get(apiId).list()).build();
	}

	// GET /apis/{apiId}/documents/{documentId} | Accept, If-None-Match, If-Modified-Since | 200. 304, 401, 404, 406
	@GET
	@Path("{apiId}/documents/{documentId}")
	public Response getAPIDocument(
			@PathParam("apiId") final String apiId,
			@PathParam("documentId") final Integer documentId,
			@HeaderParam("Accept") final String accept,
			@HeaderParam("If-None-Match") final String ifNoneMatch,
			@HeaderParam("If-Modified-Since") final String ifModifiedSince
	) {
		return Response.ok(DOCUMENTS.get(apiId).read(documentId)).build();
	}

}
