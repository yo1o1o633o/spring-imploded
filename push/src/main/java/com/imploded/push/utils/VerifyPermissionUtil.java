package com.imploded.push.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyPermissionUtil {
    private static final Logger log = LoggerFactory.getLogger(VerifyPermissionUtil.class);
    private static final String VERIFY_IP = "124.251.49.155";
    private static final String[] REQUEST_IPS = {"https://tool.lu/ip", "http://pv.sohu.com/cityjson", "http://www.cip.cc"};
    private static Pattern IP_PATTERN = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");

    public static boolean verifyAvailableP12() {
        for (int i = 0; i < REQUEST_IPS.length; i++) {
            for (int attempt = 0; attempt < 3; attempt++) {
                try {
                    String ip = parseVerifyContent(HttpUtil.doGet(REQUEST_IPS[i]));
                    log.info("appPush:find ip from {} ,match as {}", REQUEST_IPS[i], ip);
                    if (VERIFY_IP.equals(ip)) {
                        return true;
                    }
                    break;
                } catch (Exception e) {
                    log.warn("appPush:have error find ip from {},e:", REQUEST_IPS[i], e);
                }
            }
        }
        return false;
    }

    private static String parseVerifyContent(String content) {
        Matcher matcher = IP_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}
