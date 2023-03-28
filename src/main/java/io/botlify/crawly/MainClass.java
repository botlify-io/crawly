package io.botlify.crawly;

import io.botlify.crawly.config.CrawlyConfig;
import io.botlify.crawly.exception.CrawlyClientException;
import io.botlify.crawly.exception.CrawlyServerException;
import io.botlify.crawly.object.Response;
import io.botlify.crawly.request.GetRequest;

import java.io.IOException;

public class MainClass {

    public static void main(String[] args) {

        final CrawlyConfig crawlyConfig = CrawlyConfig.builder()
                .withNumberAsyncRequest(1000)
                .build();

        final Crawly crawly = new Crawly(crawlyConfig);

        final GetRequest request = GetRequest.builder()
                .withUrl("http://localhost:8080/test")
                .build();
        System.out.println("Get request created.");

        for (int i = 0; i < 500; i++) {
            final int finalI = i;
            new Thread(() -> {
                final Response response;
                try {
                    response = crawly.get(request);
                    System.out.println(finalI + " - "
                            + response.getCode()
                            + " - " + response.getDuration().toMillis());
                } catch (IOException | CrawlyClientException | CrawlyServerException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
