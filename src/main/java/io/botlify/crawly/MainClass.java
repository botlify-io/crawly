package io.botlify.crawly;

import io.botlify.crawly.config.CrawlyConfig;
import io.botlify.crawly.config.ProxyConfig;
import io.botlify.crawly.exception.CrawlyClientException;
import io.botlify.crawly.exception.CrawlyServerException;
import io.botlify.crawly.object.Response;
import io.botlify.crawly.request.GetRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;

public class MainClass {

    public static void main(String[] args) {

        final CrawlyConfig crawlyConfig = CrawlyConfig.builder()
                .withNumberAsyncRequest(5)
                .build();

        final Crawly crawly = new Crawly(crawlyConfig);

        final GetRequest request = GetRequest.builder()
                .withUrl("https://httpbin.org/get")
                .build();
        System.out.println("Get request created.");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                final Response response;
                try {
                    response = crawly.get(request);
                    System.out.println(response.getCode() + " - " + response.getBodyAsJson());
                } catch (IOException | CrawlyClientException | CrawlyServerException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
