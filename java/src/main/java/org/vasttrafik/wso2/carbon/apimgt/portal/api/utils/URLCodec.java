package org.vasttrafik.wso2.carbon.apimgt.portal.api.utils;

import javax.ws.rs.InternalServerErrorException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public final class URLCodec {

    private static final String UTF_8 = "UTF-8";

    public static String encodeUTF8(final String string) {
        if (string == null) {
            return string;
        }
        try {
            return URLEncoder.encode(string, UTF_8);
        } catch (UnsupportedEncodingException exception) {
            throw new InternalServerErrorException(exception);
        }
    }

}
