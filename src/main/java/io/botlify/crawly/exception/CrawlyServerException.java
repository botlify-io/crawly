package io.botlify.crawly.exception;

import com.sun.net.httpserver.Request;
import io.botlify.crawly.object.Response;
import org.jetbrains.annotations.NotNull;

public class CrawlyServerException extends CrawlyException {

    /**
     * This exception is throw when the code of the response
     * is between 500 and 599 included.
     * @param response The response from the server.
     */
    public CrawlyServerException(@NotNull final Response response) {
        super(response);
    }

}
