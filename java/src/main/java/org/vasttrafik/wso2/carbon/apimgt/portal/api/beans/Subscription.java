package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Subscription {

    private Application application;
    private API api;
    @SuppressWarnings("unused")
	private String tier;
    @SuppressWarnings("unused")
	private String status;
    @SuppressWarnings("unused")
	private String subStatus;

    @JsonIgnore
    private Integer applicationId;

    public static String getId(final Integer applicationId, final String apiId) {
        return applicationId + "-" + apiId;
    }

    public static Subscription valueOf(final Integer applicationId, final Application application, final API api, final String tier, final String status, final String subStatus) {
        final Subscription subscription = new Subscription();
        subscription.applicationId = applicationId;
        subscription.application = application;
        subscription.api = api;
        subscription.tier = tier;
        subscription.status = status;
        subscription.subStatus = subStatus;
        return subscription;
    }

    @JsonProperty("id")
    public String getId() {
        return Subscription.getId(applicationId, api.getId());
    }

    public API getApi() {
        return api;
    }

    public Application getApplication() {
        return application;
    }

    public Subscription setApplication(final Application application) {
        this.application = application;
        return this;
    }
}
