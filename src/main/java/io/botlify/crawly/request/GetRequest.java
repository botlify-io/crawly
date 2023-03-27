package io.botlify.crawly.request;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Builder(setterPrefix = "with")
public final class GetRequest {

    @NotNull @Getter
    private final String url;

}
