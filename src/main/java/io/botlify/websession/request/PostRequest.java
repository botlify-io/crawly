package io.botlify.websession.request;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Builder(setterPrefix = "with", builderClassName = "Builder")
public class PostRequest {

    @NotNull @Getter
    private final String url;

}
