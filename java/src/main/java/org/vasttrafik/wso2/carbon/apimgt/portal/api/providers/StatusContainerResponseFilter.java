package org.vasttrafik.wso2.carbon.apimgt.portal.api.providers;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Status;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
@Provider
public class StatusContainerResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        for (final Annotation annotation : containerResponseContext.getEntityAnnotations()) {
            if (annotation instanceof Status) {
                final Status status = (Status) annotation;
                containerResponseContext.setStatus(status.value());
                break;
            }
        }
    }

}