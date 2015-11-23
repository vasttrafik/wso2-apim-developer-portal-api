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

    public static Subscription valueOf(final Integer id, final SubscriptionsItemDTO subscriptionsItemDTO, final Application application, final API api) {
        final Subscription subscription = new Subscription();
        subscription.id = id;
        subscription.tier = subscriptionsItemDTO.tier;
        subscription.status = subscriptionsItemDTO.status;
        subscription.api = api;
        subscription.application = application;
        return subscription;
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
