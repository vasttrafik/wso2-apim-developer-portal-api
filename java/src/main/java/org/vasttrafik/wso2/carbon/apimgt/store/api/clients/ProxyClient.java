package org.vasttrafik.wso2.carbon.apimgt.store.api.clients;

import org.apache.commons.lang3.StringUtils;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.*;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.*;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class ProxyClient implements ResourceBundleAware {

    private StoreClient storeClient;

    public ProxyClient(final String username, final String password) {
        storeClient = new StoreClient(username, password);
    }


    /// Security methods

    public OauthData getDefaultApplicationOauthData() {
        final List<SubscriptionDTO> subscriptionDTOs = storeClient.getSubscriptions().subscriptions;
        for (SubscriptionDTO subscriptionDTO : subscriptionDTOs) {
            if (Constants.DEFAULT_APPLICATION.equals(subscriptionDTO.name)) {
                return OauthData.valueOf(subscriptionDTO);
            }
        }
        throw new InternalServerErrorException(ResponseUtils.serverError(resourceBundle, 2001L, new Object[][]{{Constants.DEFAULT_APPLICATION}}));
    }

    public OauthData generateDefaultApplicationOauthData(OauthData oauthData) {
        if (oauthData.getToken() != null) {
            final RefreshTokenDTO.Key key = storeClient.refreshToken(
                    oauthData.getToken(),
                    oauthData.getKey(),
                    oauthData.getSecret(),
                    oauthData.getValidityTime()
            ).data.key;
            return oauthData.setToken(key.accessToken).setKey(key.consumerKey).setSecret(key.consumerSecret);
        } else {
            final GenerateApplicationKeyDTO.Key key = storeClient.generateApplicationKey(
                    Constants.DEFAULT_APPLICATION,
                    oauthData.getValidityTime()
            ).data.key;
            return oauthData.setToken(key.accessToken);
        }
    }


    /// Token methods

    public Application generateApplicationToken(final Integer applicationId, final String validityTime) {
        final Application application = getApplication(applicationId);
        if (application.getName().equals(Constants.DEFAULT_APPLICATION)) {
            throw new BadRequestException(ResponseUtils.badRequest(resourceBundle, 2001L, new Object[][]{{Constants.DEFAULT_APPLICATION}}));
        }

        if (application.hasAccessToken()) {
            final RefreshTokenDTO.Key key = storeClient.refreshToken(application.getAccessToken(), application.getConsumerKey(), application.getConsumerSecret(), validityTime).data.key;
            return application.setAccessToken(key.accessToken);
        } else {
            final GenerateApplicationKeyDTO.Key key = storeClient.generateApplicationKey(application.getName(), validityTime).data.key;
            application.setAccessToken(key.accessToken);
            application.setConsumerKey(key.consumerKey);
            application.setConsumerSecret(key.consumerSecret);
            return application;
        }
    }


    /// API methods

    public List<API> getAPIs(final String query) {
        final List<APIDTO> apiDTOs = storeClient.getPaginatedAPIs().apis;

        final List<API> list = new ArrayList<>();
        for (final APIDTO apiDTO : apiDTOs) {
            final API api = API.valueOf(apiDTO);
            if (api.matches(query)) {
                list.add(api);
            }
        }
        return list;
    }

    public API getAPI(final String apiId) {
        try {
            return getAPIs("id:" + apiId).get(0);
        } catch (final Exception exception) {
            throw new NotFoundException(ResponseUtils.notFound(resourceBundle, 2001L, new Object[][]{{apiId}}));
        }
    }


    /// Document methods

    public List<Document> getDocuments(final API api, final String query) {
        final List<DocumentDTO> documentDTOs = storeClient.getAllDocumentationsOfApi(api.getName(), api.getVersion(), api.getProvider()).documentations;

        final List<Document> list = new ArrayList<>();
        for (final DocumentDTO documentDTO : documentDTOs) {
            final Document document = Document.valueOf(documentDTO);
            if (document.matches(query)) {
                list.add(document);
            }
        }
        return list;
    }

    public Document getDocument(final API api, final String documentId) {
        try {
            return getDocuments(api, "id:" + documentId).get(0);
        } catch (final Exception exception) {
            throw new NotFoundException(ResponseUtils.notFound(resourceBundle, 2001L, new Object[][]{{documentId}}));
        }
    }


    /// Application methods

    public List<Application> getApplications(final String query) {
        final List<ApplicationDTO> applicationDTOs = storeClient.getApplications().applications;
        final List<SubscriptionDTO> subscriptionDTOs = storeClient.getSubscriptions().subscriptions;

        final List<Application> list = new ArrayList<>();
        for (final ApplicationDTO applicationDTO : applicationDTOs) {
            for (final SubscriptionDTO subscriptionDTO : subscriptionDTOs) {
                if (subscriptionDTO.id.equals(applicationDTO.id)) {
                    final Application application = Application.valueOf(applicationDTO, subscriptionDTO);
                    if (!application.getName().equals(Constants.DEFAULT_APPLICATION) && application.matches(query)) {
                        list.add(application);
                    }
                }
            }
        }
        return list;
    }

    public Application getApplication(final Integer applicationId) {
        try {
            return getApplications("id:" + applicationId).get(0);
        } catch (final Exception exception) {
            throw new NotFoundException(ResponseUtils.notFound(resourceBundle, 2001L, new Object[][]{{applicationId}}));
        }
    }

    public Application addApplication(final Application application) {
        if (application.getName().equals(Constants.DEFAULT_APPLICATION)) {
            throw new BadRequestException(ResponseUtils.badRequest(resourceBundle, 2001L, new Object[][]{{Constants.DEFAULT_APPLICATION}}));
        }

        final StatusDTO statusDTO = storeClient.addApplication(application.getName(), application.getThrottlingTier(), application.getDescription(), application.getCallbackUrl());
        if (!"APPROVED".equals(statusDTO.status)) {
            throw new InternalServerErrorException(ResponseUtils.serverError(resourceBundle, 2007L, new Object[][]{}));
        }
        return getApplications(application.getName()).get(0);
    }

    public Application updateApplication(final Integer applicationId, final Application application) {
        final Application existingApplication = getApplication(applicationId);
        if (Constants.DEFAULT_APPLICATION.equals(existingApplication.getName()) || Constants.DEFAULT_APPLICATION.equals(application.getName())) {
            throw new BadRequestException(ResponseUtils.badRequest(resourceBundle, 2001L, new Object[][]{{Constants.DEFAULT_APPLICATION}}));
        }

        storeClient.updateApplication(
                existingApplication.getName(),
                StringUtils.defaultString(application.getName(), existingApplication.getName()),
                StringUtils.defaultString(application.getCallbackUrl(), existingApplication.getCallbackUrl()),
                StringUtils.defaultString(application.getDescription(), existingApplication.getDescription()),
                StringUtils.defaultString(application.getThrottlingTier(), existingApplication.getThrottlingTier())
        );
        return getApplication(applicationId);
    }

    public void removeApplication(final Integer applicationId) {
        final String name = getApplication(applicationId).getName();
        if (name.equals(Constants.DEFAULT_APPLICATION)) {
            throw new BadRequestException(ResponseUtils.badRequest(resourceBundle, 2001L, new Object[][]{{Constants.DEFAULT_APPLICATION}}));
        }

        storeClient.removeApplication(name);
    }


    /// Subscription methods

    public List<Subscription> getSubscriptions() {
        final List<SubscriptionDTO> subscriptionDTOs = storeClient.getSubscriptions().subscriptions;

        final List<Subscription> list = new ArrayList<>();
        for (final SubscriptionDTO subscriptionDTO : subscriptionDTOs) {
            if (!subscriptionDTO.name.equals(Constants.DEFAULT_APPLICATION)) {
                for (final SubscriptionsItemDTO subscriptionsItemDTO : subscriptionDTO.subscriptions) {
                    final Application application = getApplication(subscriptionDTO.id);
                    final API api = getAPI(API.getId(subscriptionsItemDTO.name, subscriptionsItemDTO.version, subscriptionsItemDTO.provider));
                    list.add(Subscription.valueOf(subscriptionDTO.id, subscriptionsItemDTO, application, api));
                }
            }
        }
        return list;
    }

    public Subscription getSubscription(final Integer id) {
        final List<Subscription> subscriptions = getSubscriptions();
        for (Subscription subscription : subscriptions) {
            if (subscription.getId().equals(id)) {
                return subscription;
            }
        }
        throw new NotFoundException(ResponseUtils.notFound(resourceBundle, 2001L, new Object[][]{{String.valueOf(id)}}));
    }

    public Subscription addSubscription(final Subscription subscription) {
        final API api = subscription.getApi();
        final Application application = subscription.getApplication();
        storeClient.addSubscription(api.getName(), api.getVersion(), api.getProvider(), application.getThrottlingTier(), application.getName());
        return getSubscription(subscription.getId());
    }

    public void removeSubscription(final Subscription subscription) {
        final API api = subscription.getApi();
        storeClient.removeSubscription(api.getName(), api.getVersion(), api.getProvider(), subscription.getId());
    }

}
