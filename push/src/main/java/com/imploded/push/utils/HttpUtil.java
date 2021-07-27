package com.imploded.push.utils;

import com.google.common.base.Throwables;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class HttpUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static String doGet(String url) throws IOException {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result;
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("User-Agent","curl/7.58.0");
            // 设置配置请求参数
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);

            if (response.getStatusLine().getStatusCode() == 200) {
                return result;
            } else {
                log.warn("HttpGetException http-get,request={}，状态码:{},内容：{}", url, response.getStatusLine().getStatusCode(), result);
                return result;
            }
        } catch (Exception e) {
            log.warn("HttpGetException http-get执行时出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
            throw e;
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.warn("HttpGetException http-get关闭response出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.warn("HttpGetException http-get关闭httpClient出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
        }
    }
}
