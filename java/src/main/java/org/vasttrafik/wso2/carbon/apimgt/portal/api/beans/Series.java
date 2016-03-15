package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import java.util.ArrayList;
import java.util.List;

public class Series {

	private String name;
	private List<String> names;
	private List<Double> values;
	
	public Series(String name) {
		this.name = name;
		this.names = new ArrayList<String>();
		this.values = new ArrayList<Double>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Double> getValues() {
		return values;
	}
	public void setValues(List<Double> value) {
		this.values = value;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	
}
