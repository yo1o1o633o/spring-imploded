package com.imploded.push.apns;

import com.imploded.push.utils.VerifyPermissionUtil;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author shuai.yang
 */
public class ApnsConnect {
    private static ApnsClient apnsClient = null;

    /**
     * 设置处理线程
     * */
    protected static int nioEventLoopGroupCount;

    /**
     * 服务器与苹果服务器建立链接通道的个数
     * */
    protected static int concurrentConnectionCount;

    /**
     * APNS HOST
     * */
    private static String APNS_HOST;

    /**
     * apns证书
     * */
    private static String APNS_CREDENTIALS_FILE;

    /**
     * apns证书密码
     * */
    private static String APNS_CREDENTIALS_PASSWORD;

    /**
     * apns包名topic
     * */
    public static String APNS_PACKAGE_TOPIC;

    static ApnsClient getAPNSConnect() {
        if (apnsClient == null) {
            try {
                InputStream inStream = ApnsConnect.class.getResourceAsStream("/" + APNS_CREDENTIALS_FILE);

                EventLoopGroup eventLoopGroup = new NioEventLoopGroup(nioEventLoopGroupCount);
                apnsClient = new ApnsClientBuilder().build();
            } catch (SSLException e) {
                e.printStackTrace();
            }
        }
        return apnsClient;
    }

    static void initApnsClient(String env) throws IOException {
        if ("debug".equalsIgnoreCase(env)) {
            //测试app分包
            APNS_HOST = ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
            APNS_CREDENTIALS_FILE = "Push_dev.p12";
            APNS_CREDENTIALS_PASSWORD = "password";
            APNS_PACKAGE_TOPIC = "com.imploded.push";
        } else if ("release".equalsIgnoreCase(env)) {
            //线上app主包
            if (VerifyPermissionUtil.verifyAvailableP12()) {
                APNS_HOST = ApnsClientBuilder.PRODUCTION_APNS_HOST;
                APNS_CREDENTIALS_FILE = "Push_Release.p12";
                APNS_CREDENTIALS_PASSWORD = "password";
                APNS_PACKAGE_TOPIC = "com.imploded.push";
            } else {
                throw new IOException("appPush:apns:have no permission to run push!");
            }
        } else {
            throw new IOException("环境只能被设置为debug或release");
        }
        if(apnsClient != null) {
            apnsClient.close();
            apnsClient = null;
        }
    }
}
