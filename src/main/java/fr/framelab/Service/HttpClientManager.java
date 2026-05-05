package fr.framelab.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;

public class HttpClientManager {
    private static HttpClient client;

    public static HttpClient getClient() {
        if (client == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            client = HttpClient.newBuilder()
                    .cookieHandler(cookieManager)
                    .build();
        }
        return client;
    }

}
