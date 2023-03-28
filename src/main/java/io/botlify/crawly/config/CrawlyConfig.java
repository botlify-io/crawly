package io.botlify.crawly.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true, setterPrefix = "with")
@EqualsAndHashCode
@ToString
public class CrawlyConfig {

    /**
     * Default configuration object.
     */
    public static final CrawlyConfig DEFAULT = CrawlyConfig.builder()
            .build();

    /**
     * Number of async request to execute.
     */
    @Getter
    @Range(from = 0, to = Integer.MAX_VALUE)
    @Builder.Default
    private final int numberAsyncRequest = 1;

    /**
     * List of proxy configuration.
     */
    @Getter @Nullable
    @Builder.Default
    private final ProxyConfig proxyConfig = null;

    /**
     * The cookie store to use.
     */
    @Getter @NotNull
    @Builder.Default
    private final BasicCookieStore cookieStore = new BasicCookieStore();

    /**
     * The default request configuration.
     */
    @Getter @Nullable
    @Builder.Default
    private final RequestConfig defaultRequestConfig = RequestConfig.DEFAULT;

}
