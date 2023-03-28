package io.botlify.crawly;

import io.botlify.crawly.config.CrawlyConfig;
import io.botlify.crawly.config.ProxyConfig;
import io.botlify.crawly.exception.CrawlyClientException;
import io.botlify.crawly.exception.CrawlyServerException;
import io.botlify.crawly.object.Response;
import io.botlify.crawly.request.GetRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.Semaphore;

@EqualsAndHashCode
@ToString
@Log4j2
public final class Crawly {

    /**
     * The configuration used by the Crawly instance.
     */
    @NotNull @Getter
    private final CrawlyConfig config;

    /**
     * The cookie store used to store cookies.
     */
    @NotNull @Getter
    private final BasicCookieStore cookieStore;

    /**
     * The HTTP client used to make requests.
     */
    @NotNull
    private final CloseableHttpClient httpClient;

    /**
     * A fair semaphore to limit the number of concurrent requests.
     */
    @NotNull
    private final Semaphore semaphore;


    /*
     $      Constructor
     */

    public Crawly() {
        this(CrawlyConfig.DEFAULT);
    }

    public Crawly(@NotNull final CrawlyConfig config) {
        this.config = config.toBuilder().build();
        this.httpClient = newHttpClient();
        this.semaphore = new Semaphore(config.getNumberAsyncRequest(), true);
        this.cookieStore = config.getCookieStore();
    }

    /*
     $      Private methods
     */

    @SneakyThrows
    private CloseableHttpClient newHttpClient() {
        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        /*
         * This line below allow redirection (301, 302, 303, 307, 308) to be followed automatically
         * in the case of a POST request. By default, the HttpClient does not follow redirections for POST requests.
         * This is specified in the HTTP specification. (HTTP RFC 2616)
         */
        httpClientBuilder.setRedirectStrategy(new DefaultRedirectStrategy());

        /*
         * Create a SSLContext that uses our Trust Strategy to trust all self-signed certificates.
         */
        final SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        final SSLContext sslContext = sslContextBuilder.loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
        final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);

        final RegistryBuilder<ConnectionSocketFactory> regBuilder = RegistryBuilder.create();
        regBuilder.register("https", sslConnectionSocketFactory);
        regBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = regBuilder.build();

        /*
         * PoolingHttpClientConnectionManager allow to reuse connections
         * and avoid the creation of new connections when the HttpClient is used in multiple threads.
         * This is useful when the HttpClient is used in a multithreaded environment.
         */
        final PoolingHttpClientConnectionManager poolingClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingClientConnectionManager.setDefaultMaxPerRoute(config.getNumberAsyncRequest());
        poolingClientConnectionManager.setMaxTotal(config.getNumberAsyncRequest());
        /*
         * Setup all the proxy configuration.
         */
        if (config.getProxyConfig() != null) {
            final ProxyConfig proxyConfig = config.getProxyConfig();

            final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            final HttpHost httpHostProxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
            if (proxyConfig.hasValidCredentials()) {
                assert proxyConfig.getUsername() != null;
                assert proxyConfig.getPassword() != null;
                final UsernamePasswordCredentials credentials =
                        new UsernamePasswordCredentials(proxyConfig.getUsername(),
                                proxyConfig.getPassword().toCharArray());
                credentialsProvider.setCredentials(new AuthScope(httpHostProxy), credentials);

            }
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setProxy(httpHostProxy);
        }
        /*
         * Build the HttpClient for the client with HttpClientBuilder.
         */
        httpClientBuilder.setDefaultCookieStore(cookieStore);
        httpClientBuilder.setConnectionManager(poolingClientConnectionManager);
        return (httpClientBuilder.build());
    }

    /*
     $      Request
     */

    @SneakyThrows({InterruptedException.class})
    public @NotNull Response get(@NotNull final GetRequest request) throws IOException, CrawlyClientException,
            CrawlyServerException {
        final Instant start = Instant.now();
        semaphore.acquire();
        try {
            final HttpGet httpGet = new HttpGet(request.getUrl());
            return (new Response(start, httpClient.execute(httpGet)));
        } finally {
            semaphore.release();
        }
    }

}
