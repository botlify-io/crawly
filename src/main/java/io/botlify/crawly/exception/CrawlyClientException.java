package io.botlify.crawly.exception;

import io.botlify.crawly.object.Response;
import org.jetbrains.annotations.NotNull;

public class CrawlyClientException extends CrawlyException {

    /**
     * This exception is throw when the code of the response
     * is between 400 and 499 included.
     * @param response The response from the server.
     */
    public CrawlyClientException(@NotNull final Response response) {
        super(response);
    }

}
