package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.SubscriptionsItemDTO;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Subscription {

    private Integer id;
    private Application application;
    private API api;
    private String tier;
    private String status;

    public Subscription() {}

    public Subscription(final Integer id, final SubscriptionsItemDTO subscriptionsItemDTO, final Application application, final API api) {
        this.id = id;
        tier = subscriptionsItemDTO.tier;
        status = subscriptionsItemDTO.status;
        this.api = api;
        this.application = application;
    }

    public API getApi() {
        return api;
    }

    public Application getApplication() {
        return application;
    }

    public Integer getId() {
        return id;
    }
}
