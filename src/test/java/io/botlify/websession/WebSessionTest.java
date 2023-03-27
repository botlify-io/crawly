package io.botlify.websession;

import io.botlify.websession.request.GetRequest;
import org.junit.jupiter.api.Test;

class WebSessionTest {

    @Test
    void builder() {
        WebSession webSession = WebSession.builder()
                .withNumberAsyncRequest(10)
                .build();

        GetRequest getRequest = new GetRequest.Builder()
                .build();

    }

}