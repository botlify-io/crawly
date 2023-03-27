package io.botlify.crawly.exception;

import io.botlify.crawly.object.Response;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class CrawlyException extends Exception {

    @NotNull @Getter
    private final Response requestResponse;

    public CrawlyException(@NotNull final Response requestResponse) {
        super("Response of request return status code: " + requestResponse.getCode());
        this.requestResponse = requestResponse;
    }

}
