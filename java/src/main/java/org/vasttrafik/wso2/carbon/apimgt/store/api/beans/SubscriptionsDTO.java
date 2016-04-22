package org.vasttrafik.wso2.carbon.apimgt.store.api.beans;

import java.util.List;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class SubscriptionsDTO extends WrapperDTO {
	
	public class ApplicationsDTO {
		public List<SubscriptionDTO> applications;
		public Integer totalLength;
	}

    //public List<SubscriptionDTO> subscriptions;
    public ApplicationsDTO subscriptions;
}
