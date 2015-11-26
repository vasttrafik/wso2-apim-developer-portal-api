package org.vasttrafik.wso2.carbon.apimgt.store.api.clients;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.store.api.beans.*;
import org.vasttrafik.wso2.carbon.common.api.utils.ClientUtils;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

/**
 * Java API for WSO2 1.8 Store APIs: https://docs.wso2.com/display/AM180/Store+APIs
 * <p/>
 * This class will create a session cookie based on the provided credentials.
 *
 * NotAuthorizedException(message) will be thrown if the credentials could note be used to login
 * InternalServerErrorException(message) is thrown if a response contains "error":"true"
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
final class StoreClient implements ResourceBundleAware {

    private static final WebTarget TARGET;

    static {
        final int port = 9763 + ClientUtils.PORT_OFFSET;
        final String baseUrl = "http://" + ClientUtils.HOST_NAME + ":" + port + "/store/site/blocks/";
        TARGET = ClientBuilder.newClient().target(baseUrl);
    }

    private final Cookie cookie;

    StoreClient(final String username, final String password) {
        cookie = login(username, password);
    }

    private Cookie login(final String username, final String password) {
        final Response response = TARGET.path("user/login/ajax/login.jag").queryParam(Constants.ACTION, "login")
                .queryParam("username", username).queryParam("password", password)
                .request().post(null);
        final WrapperDTO wrapperDTO = response.readEntity(WrapperDTO.class);
        if (BooleanUtils.isNotFalse(wrapperDTO.error)) {
            throw new NotAuthorizedException(ResponseUtils.notAuthorizedError(resourceBundle, 2002L, new Object[][]{{wrapperDTO.message}}));
        }
        return response.getCookies().get("JSESSIONID");
    }

    private <T extends WrapperDTO> T validateDTO(final T wrapperDTO) {
        if (BooleanUtils.isNotFalse(wrapperDTO.error)) {
            throw new InternalServerErrorException(ResponseUtils.serverError(resourceBundle, 2002L, new Object[][]{{wrapperDTO.message}}));
        }
        return wrapperDTO;
    }

    GenerateApplicationKeyDTO generateApplicationKey(final String name, final String validityTime) {
        final Form form = new Form()
                .param(Constants.ACTION, "generateApplicationKey")
                .param("application", name)
                .param("keytype", Constants.KEYTYPE_PRODUCTION)
                .param("callbackUrl", "")
                .param("authorizedDomains", Constants.AUTHORIZED_DOMAINS_ALL)
                .param("validityTime", validityTime);

        return validateDTO(TARGET.path("subscription/subscription-add/ajax/subscription-add.jag")
                .request().cookie(cookie).post(Entity.form(form), GenerateApplicationKeyDTO.class));
    }

    /**
     * This method is not listed in the official documentation
     */
    RefreshTokenDTO refreshToken(final String oldAccessToken, final String clientKey, final String clientSecret, final String validityTime) {
        final Form form = new Form()
                .param(Constants.ACTION, "refreshToken")
                .param("application", Constants.DEFAULT_APPLICATION)
                .param("keytype", Constants.KEYTYPE_PRODUCTION)
                .param("oldAccessToken", oldAccessToken)
                .param("authorizedDomains", Constants.AUTHORIZED_DOMAINS_ALL)
                .param("clientId", clientKey)
                .param("clientSecret", clientSecret)
                .param("validityTime", validityTime);

        return validateDTO(TARGET.path("subscription/subscription-add/ajax/subscription-add.jag")
                .request().cookie(cookie).post(Entity.form(form), RefreshTokenDTO.class));
    }

    /**
     * The response of this method may be specific to Västtrafiks installation of WSO2
     */
    APIsDTO getPaginatedAPIs() {
        return validateDTO(TARGET.path("api/listing/ajax/list.jag").queryParam(Constants.ACTION, "getPaginatedAPIs")
                .queryParam("tenant", "carbon.super")
                .request().cookie(cookie).get(APIsDTO.class));
    }

    DocumentsDTO getAllDocumentationsOfApi(final String name, final String version, final String provider) {
        return validateDTO(TARGET.path("api/listing/ajax/list.jag")
                .queryParam(Constants.ACTION, "getAllDocumentationOfApi")
                .queryParam("name", name)
                .queryParam("version", version)
                .queryParam("provider", provider)
                .request().cookie(cookie).get(DocumentsDTO.class));
    }

    ApplicationsDTO getApplications() {
        return validateDTO(TARGET.path("application/application-list/ajax/application-list.jag")
                .queryParam(Constants.ACTION, "getApplications")
                .request().cookie(cookie).get(ApplicationsDTO.class));
    }

    StatusDTO addApplication(final String name, final String tier, final String description, final String callbackUrl) {
        final Form form = new Form()
                .param(Constants.ACTION, "addApplication")
                .param("application", StringUtils.defaultString(name))
                .param("tier", StringUtils.defaultString(tier, Constants.TIER_UNLIMITED))
                .param("description", StringUtils.defaultString(description))
                .param("callbackUrl", StringUtils.defaultString(callbackUrl));

        return validateDTO(TARGET.path("application/application-add/ajax/application-add.jag")
                .request().cookie(cookie).post(Entity.form(form), StatusDTO.class));
    }

    WrapperDTO updateApplication(final String name, final String newName, final String callbackUrl, final String description, final String tier) {
        final Form form = new Form()
                .param(Constants.ACTION, "updateApplication")
                .param("applicationOld", name)
                .param("applicationNew", StringUtils.defaultString(newName, name))
                .param("callbackUrlNew", StringUtils.defaultString(callbackUrl))
                .param("descriptionNew", StringUtils.defaultString(description))
                .param("tier", StringUtils.defaultString(tier, Constants.TIER_UNLIMITED));

        return validateDTO(TARGET.path("application/application-update/ajax/application-update.jag")
                .request().cookie(cookie).post(Entity.form(form), WrapperDTO.class));
    }

    WrapperDTO removeApplication(final String name) {
        final Form form = new Form()
                .param(Constants.ACTION, "removeApplication")
                .param("application", name);

        return validateDTO(TARGET.path("application/application-remove/ajax/application-remove.jag")
                .request().cookie(cookie).post(Entity.form(form), WrapperDTO.class));
    }

    SubscriptionsDTO getAllSubscriptions() {
        return validateDTO(TARGET.path("subscription/subscription-list/ajax/subscription-list.jag")
                .queryParam(Constants.ACTION, "getAllSubscriptions")
                .request().cookie(cookie).get(SubscriptionsDTO.class));
    }

    /**
     * This is required because there is a Store API bug that makes getAllSubscriptions only returns subscriptions
     * for a one of the applications in the list.
     */
    SubscriptionByApplicationsDTO getSubscriptionsByApplication(final String applicationName) {
        return validateDTO(TARGET.path("subscription/subscription-list/ajax/subscription-list.jag")
                .queryParam(Constants.ACTION, "getSubscriptionByApplication")
                .queryParam("app", applicationName)
                .request().cookie(cookie).get(SubscriptionByApplicationsDTO.class));
    }

    /**
     * Invoking this method works fine and the result shows up in the store web app. It does NOT however show up in getAllSubscriptions result.
     * Also note that the Store API does not seem to support the documented version where application id is used instead of application name.
     */
    WrapperDTO addSubscription(final String apiName, final String apiVersion, final String apiProvider, final String applicationTier, final String applicationName) {
        final Form form = new Form()
                .param(Constants.ACTION, "addAPISubscription")
                .param("name", apiName)
                .param("version", apiVersion)
                .param("provider", apiProvider)
                .param("tier", applicationTier)
                .param("applicationName", applicationName);

        return validateDTO(TARGET.path("subscription/subscription-add/ajax/subscription-add.jag")
                .request().cookie(cookie).post(Entity.form(form), WrapperDTO.class));
    }

    /**
     * Note that the Store API does not seem to support the documented version where application name is used instead of application id.
     * Invoking the url works but the results is not visible when listing all subscriptions via the api or in the store web app.
     */
    WrapperDTO removeSubscription(final String name, final String version, final String provider, final Integer applicationId) {
        final Form form = new Form()
                .param(Constants.ACTION, "removeSubscription")
                .param("name", name)
                .param("version", version)
                .param("provider", provider)
                .param("applicationId", String.valueOf(applicationId));

        return validateDTO(TARGET.path("subscription/subscription-remove/ajax/subscription-remove.jag")
                .request().cookie(cookie).post(Entity.form(form), WrapperDTO.class));
    }

}
