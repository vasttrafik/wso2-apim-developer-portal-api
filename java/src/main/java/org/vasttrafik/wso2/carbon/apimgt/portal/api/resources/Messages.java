package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Authorization;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Status;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Message;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.MailSender;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Authorization
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("messages")
public class Messages implements ResourceBundleAware {

	@Context
    private SecurityContext securityContext;
	
    @Status(Status.CREATED)
    @POST
    public void postMessage(final Message message) {
        ResponseUtils.checkParameter(resourceBundle, "from", true, new String[]{}, message.getFrom()); 
        ResponseUtils.checkParameter(resourceBundle, "subject", true, new String[]{}, message.getSubject());
        ResponseUtils.checkParameter(resourceBundle, "body", true, new String[]{}, message.getBody());
		
		String messageType = message.getMessageType();
		String contentType = message.getContentType();
		
		if (messageType == null)
		  messageType = "EMAIL";
		  
		ResponseUtils.checkParameter(resourceBundle, "messageType", true, new String[]{"EMAIL", "INTERNAL"}, messageType);
		
		if (contentType == null)
		  contentType = "text/plain";
		  
		ResponseUtils.checkParameter(resourceBundle, "contentType", true, new String[]{"text/plain", "text/html"}, contentType);

        try {
            MailSender.send(message);
        }
        catch (final Exception exception) {
            throw new InternalServerErrorException(ResponseUtils.serverError(exception));
        }
    }
}