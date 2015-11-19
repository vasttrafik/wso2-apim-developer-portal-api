package org.vasttrafik.wso2.carbon.apimgt.store.api.clients;

import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.*;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class ProxyClient {

    private StoreClient storeClient;

    public ProxyClient(final String username, final String password) {
        storeClient = new StoreClient(username, password);
    }


    /// Security methods

    public OauthData getDefaultApplicationOauthData() {
        final List<SubscriptionDTO> subscriptionDTOs = storeClient.getSubscriptions().subscriptions;
        for (SubscriptionDTO subscriptionDTO : subscriptionDTOs) {
            if (Constants.DEFAULT_APPLICATION.equals(subscriptionDTO.name)) {
                final OauthData oauthData = new OauthData();
                oauthData.token = subscriptionDTO.prodKey;
                oauthData.key = subscriptionDTO.prodConsumerKey;
                oauthData.secret = subscriptionDTO.prodConsumerSecret;
                oauthData.validityTime = String.valueOf(subscriptionDTO.prodValidityTime);
                return oauthData;
            }
        }
        throw new InternalServerErrorException();
    }

    public OauthData refreshDefaultApplicationOauthData(OauthData oauthData) {
        final RefreshTokenDTO.Key key = storeClient.refreshToken(oauthData.token, oauthData.key, oauthData.secret, oauthData.validityTime).data.key;
        oauthData.token = key.accessToken;
        return oauthData;
    }


    /// Token methods

    public Application generateApplicationToken(final Integer applicationId, final String validityTime) {
        final Application application = getApplication(applicationId);
        if (application.getName().equals(Constants.DEFAULT_APPLICATION)) {
            throw new BadRequestException();
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
            final API api = new API(apiDTO);
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
            throw new NotFoundException();
        }
    }


    /// Document methods

    public List<Document> getDocuments(final API api, final String query) {
        final List<DocumentDTO> documentDTOs = storeClient.getAllDocumentationsOfApi(api.getName(), api.getVersion(), api.getProvider()).documentations;

        final List<Document> list = new ArrayList<>();
        for (final DocumentDTO documentDTO : documentDTOs) {
            final Document document = new Document(documentDTO);
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
            throw new NotFoundException();
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
                    final Application application = new Application(applicationDTO, subscriptionDTO);
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
            throw new NotFoundException();
        }
    }

    public Application addApplication(final Application application) {
        if (application.getName().equals(Constants.DEFAULT_APPLICATION)) {
            throw new BadRequestException();
        }

        final StatusDTO statusDTO = storeClient.addApplication(application.getName(), application.getThrottlingTier(), application.getDescription(), application.getCallbackUrl());
        if (!"APPROVED".equals(statusDTO.status)) {
            throw new InternalServerErrorException("status != APPROVED");
        }
        return getApplications(application.getName()).get(0);
    }

    public Application updateApplication(final Integer applicationId, final Application application) {
        final String name = getApplication(applicationId).getName();
        if (name.equals(Constants.DEFAULT_APPLICATION) || application.getName().equals(Constants.DEFAULT_APPLICATION)) {
            throw new BadRequestException();
        }

        storeClient.updateApplication(name, application.getName(), application.getCallbackUrl(), application.getDescription(), application.getThrottlingTier());
        return getApplication(applicationId);
    }

    public void removeApplication(final Integer applicationId) {
        final String name = getApplication(applicationId).getName();
        if (name.equals(Constants.DEFAULT_APPLICATION)) {
            throw new BadRequestException();
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
                    final Subscription subscription = new Subscription(subscriptionDTO.id, subscriptionsItemDTO, application, api);
                    list.add(subscription);
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
        throw new NotFoundException();
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
