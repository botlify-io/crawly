package io.botlify.crawly.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EqualsAndHashCode
@ToString
public class ProxyConfig {

    @NotNull @Getter
    private final String host;

    @Getter
    private final int port;

    @Nullable @Getter
    private final String username;

    @Nullable @Getter
    private final String password;

    protected ProxyConfig(@NotNull final builder builder) {
        if (builder.host == null || builder.host.isEmpty())
            throw (new IllegalArgumentException("Host for proxy configuration cannot be null or empty"));
        if (builder.port <= 0)
            throw (new IllegalArgumentException("Port for proxy configuration cannot be 0 or negative"));
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
    }

    public boolean hasValidCredentials() {
        return (username != null && password != null);
    }

    public @NotNull String toConfigString() {
        String result = host + ":" + port;
        if (hasValidCredentials()) {
            result += ":" + username + ":" + password;
        }
        return (result);
    }

    public static class builder {

        private String host;

        private int port;

        private String username;

        private String password;

        public @NotNull builder hostAndPort(@NotNull final String host,
                                            final int port) {
            this.host = host;
            this.port = port;
            return (this);
        }

        public @NotNull builder usernameAndPassword(@NotNull final String username,
                                                    @NotNull final String password) {
            this.username = username;
            this.password = password;
            return (this);
        }

        public @NotNull ProxyConfig build() {
            return (new ProxyConfig(this));
        }

    }

}
