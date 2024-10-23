package com.rnpc.operatingunit.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class ClientIpUtil {
    private static final String CLIENT_IP_HEADER = "X-Forwarded-For";

    public static String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader(CLIENT_IP_HEADER);
        if (StringUtils.isBlank(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }
}

