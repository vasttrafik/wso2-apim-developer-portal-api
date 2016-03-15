package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import java.util.ArrayList;
import java.util.List;

public class Statistic {
	
	private String api;
	private String apiVersion;
	private String type;
	private List<Series> series;
	
	public Statistic(String type) {
		
		this.series = new ArrayList<Series>();
		this.type = type;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
