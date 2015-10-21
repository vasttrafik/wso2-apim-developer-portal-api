package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Endpoint {

	public enum Type {

		@JsonProperty("http")
		HTTP,
		@JsonProperty("address")
		ADDRESS,
		@JsonProperty("wsdl")
		WSDL,
		@JsonProperty("failover")
		FAILOVER,
		@JsonProperty("load_balanced")
		LOAD_BALANCED
	}

	private Type type;
	private List<String> production;
	private List<String> sandbox;

	public Endpoint() {
	}

	public Endpoint(Type type, List<String> production, List<String> sandbox) {
		this.type = type;
		this.production = production;
		this.sandbox = sandbox;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<String> getProduction() {
		return production;
	}

	public void setProduction(List<String> production) {
		this.production = production;
	}

	public List<String> getSandbox() {
		return sandbox;
	}

	public void setSandbox(List<String> sandbox) {
		this.sandbox = sandbox;
	}

}
