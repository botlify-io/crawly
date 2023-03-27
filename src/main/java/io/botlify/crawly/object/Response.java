package io.botlify.crawly.object;

import io.botlify.crawly.exception.CrawlyClientException;
import io.botlify.crawly.exception.CrawlyServerException;
import lombok.Getter;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Response {

    /**
     * Body of the response as a byte array.
     */
    @Getter
    private final byte @NotNull [] bodyAsByteArray;

    /**
     * Status code of the response.
     */
    @Getter
    private final int code;

    /**
     * Headers of the response.
     */
    @NotNull @Getter
    private final List<Header> headers;

    /**
     * Duration between the start of the request and the end of the response.
     */
    @NotNull @Getter
    private final Duration duration;

    public Response(@NotNull final Instant startDate,
                    @NotNull final CloseableHttpResponse response) throws IOException, CrawlyClientException, CrawlyServerException {
        try (response) {
            this.duration = Duration.between(startDate, Instant.now());
            this.headers = new ArrayList<>(response.getHeaders().length);
            headers.addAll(Arrays.asList(response.getHeaders()));
            this.code = response.getCode();
            // Read the body of the response.
            this.bodyAsByteArray = EntityUtils.toByteArray(response.getEntity());
            // Assert the response.
            assertResponse();
        }
    }

    /*
     $      Public methods
     */

    public @NotNull String getBodyAsString() {
        return (new String(bodyAsByteArray));
    }

    public @NotNull JSONObject getBodyAsJson() {
        return (new JSONObject(getBodyAsString()));
    }

    /*
     $      Private methods
     */

    private void assertResponse() throws CrawlyClientException, CrawlyServerException {
        if (code >= 400 && code < 499)
            throw new CrawlyClientException(this);
        if (code >= 500 && code < 599)
            throw new CrawlyServerException(this);
    }

}
