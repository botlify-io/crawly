package io.botlify.crawly.request;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Builder(setterPrefix = "with")
public final class PostRequest {

    @NotNull @Getter
    private final String url;

}
