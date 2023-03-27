package io.botlify.websession;

import io.botlify.websession.config.ProxyConfig;
import io.botlify.websession.object.RequestResponse;
import io.botlify.websession.request.GetRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;
import java.util.List;

public class MainClass {

    public static void main(String[] args) throws IOException {
        ProxyConfig proxyConfig = new ProxyConfig.builder()
                .hostAndPort("localhost", 8080)
                .build();

        final WebSession webSession = new WebSession.Builder()
                .withNumberAsyncRequest(1)
                .build();

        GetRequest request = GetRequest.builder()
                .withUrl("https://httpbin.org/get")
                .build();
        System.out.println("get request created");


        CloseableHttpResponse closeableHttpResponse = webSession.get(request);

        System.out.println(closeableHttpResponse.getCode());
    }

}
