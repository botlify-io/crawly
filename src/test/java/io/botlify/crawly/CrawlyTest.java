package io.botlify.crawly;

import io.botlify.crawly.request.GetRequest;
import org.junit.jupiter.api.Test;

class CrawlyTest {

    @Test
    void builder() {
        Crawly crawly = Crawly.builder()
                .withNumberAsyncRequest(10)
                .build();

        GetRequest getRequest = new GetRequest.Builder()
                .build();

    }

}