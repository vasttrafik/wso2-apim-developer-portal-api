package org.vasttrafik.wso2.carbon.apimgt.store.api.beans;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class ApplicationDTO {

    public String name;
    public String tier;
    public Integer id;
    public String callbackUrl;
    public String status;
    public String description;
    // New attribute
    public Integer apiCount;
    public Integer groupId;
}
