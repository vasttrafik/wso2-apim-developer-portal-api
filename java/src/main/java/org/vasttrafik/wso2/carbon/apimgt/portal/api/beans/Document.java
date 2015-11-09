package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Document {

	public enum Type {

		@JsonProperty("file")
		FILE,
		@JsonProperty("inline")
		INLINE,
		@JsonProperty("url")
		URL
	}

	private Integer id;
	private String name;
	private Type type;
	private String summary;
	private String content;
	private String url;

	public Document() {
	}

	public Document(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
