package io.botlify.crawly.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder(setterPrefix = "with", builderClassName = "Builder")
@ToString
@EqualsAndHashCode
public class RequestConfig {

    public static final RequestConfig DEFAULT = RequestConfig.builder()
            .withTimeoutConnectionMillis(60000)
            .withTimeoutConnectionMillis(60000)
            .withTimeoutRequestMillis(60000)
            .build();

    private final int timeoutSocketMillis;

    private final int timeoutConnectionMillis;

    private final int timeoutRequestMillis;

}
