package fr.framelab.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mizosoft.methanol.Methanol;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;

public class HttpClientManager {
    private static Methanol client;

    public static Methanol getClient() {
        if (client == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            client = Methanol.newBuilder()
                    .cookieHandler(cookieManager)
                    .build();
        }
        return client;
    }

}
