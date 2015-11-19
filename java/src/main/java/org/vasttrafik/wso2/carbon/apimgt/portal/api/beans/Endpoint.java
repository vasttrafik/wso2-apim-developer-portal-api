package org.vasttrafik.wso2.carbon.apimgt.portal.api.beans;

import java.util.List;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class Endpoint {

    public enum Type {
        http, address, wsdl, failover, load_balanced
    }

    private Type type;
    private List<String> production;
    private List<String> sandbox;

}
