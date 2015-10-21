package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import java.util.Date;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class API {

	public enum Transport {

		@JsonProperty("http")
		HTTP,
		@JsonProperty("https")
		HTTPS
	}

	public enum Visibility {

		@JsonProperty("public")
		PUBLIC,
		@JsonProperty("private")
		PRIVATE

	}

	private String name;
	private String description;
	private String imageUrl;
	private String context;
	private String version;
	private String provider;
	private String swagger;
	private String status;
	private String responseCaching;
	private Date lastModifiedDate;
	private Boolean isDefaultVersion;
	private List<Transport> transports;
	private String tier;
	private Visibility visibility;
	private Endpoint endpoint;

	public API() {
	}

	public API(String name, String context, String version) {
		this.name = name;
		this.context = context;
		this.version = version;
	}

	// Computed property
	public String getId() {
		return String.format("%s-%s-%s", name, version, this.provider != null ? this.provider : "null");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getSwagger() {
		return swagger;
	}

	public void setSwagger(String swagger) {
		this.swagger = swagger;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseCaching() {
		return responseCaching;
	}

	public void setResponseCaching(String responseCaching) {
		this.responseCaching = responseCaching;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Boolean getIsDefaultVersion() {
		return isDefaultVersion;
	}

	public void setIsDefaultVersion(Boolean isDefaultVersion) {
		this.isDefaultVersion = isDefaultVersion;
	}

	public List<Transport> getTransports() {
		return transports;
	}

	public void setTransports(List<Transport> transport) {
		this.transports = transport;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

}
