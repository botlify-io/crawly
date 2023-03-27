package io.botlify.websession;

import io.botlify.websession.request.GetRequest;
import io.botlify.websession.request.PostRequest;
import io.botlify.websession.config.ProxyConfig;
import io.botlify.websession.config.RequestConfig;
import io.botlify.websession.object.RequestResponse;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
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
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Builder(setterPrefix = "with", builderClassName = "Builder")
@EqualsAndHashCode
@ToString
public class WebSession {

    @Getter @Range(from = 0, to = Integer.MAX_VALUE) @lombok.Builder.Default
    private final int numberAsyncRequest = 1;

    @Getter @NotNull @lombok.Builder.Default
    private final List<ProxyConfig> proxyConfigs = new ArrayList<>(0);

    @Getter @NotNull @lombok.Builder.Default
    private final BasicCookieStore cookieStore = new BasicCookieStore();

    @Getter @Nullable @lombok.Builder.Default
    private final RequestConfig defaultRequestConfig = RequestConfig.DEFAULT;


    /*
     $      Constructor
     */

    @SneakyThrows
    public CloseableHttpClient newHttpClient() {
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
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        SSLContext sslContext = sslContextBuilder.loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);

        RegistryBuilder<ConnectionSocketFactory> regBuilder = RegistryBuilder.create();
        regBuilder.register("https", sslConnectionSocketFactory);
        regBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = regBuilder.build();

        /*
         * PoolingHttpClientConnectionManager allow to reuse connections
         * and avoid the creation of new connections when the HttpClient is used in multiple threads.
         * This is useful when the HttpClient is used in a multithreaded environment.
         */
        PoolingHttpClientConnectionManager poolingClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // poolingClientConnectionManager.setValidateAfterInactivity((int) (getTimeout() / 2));
        // poolingClientConnectionManager.setMaxTotal();
        // poolingClientConnectionManager.setDefaultMaxPerRoute(MAX_SYNCHRONOUS_REQUEST == 0 ? 3 : MAX_SYNCHRONOUS_REQUEST);

        /*
         * Verify if the Bright data proxy is enabled.
         * If it is enabled, the HttpClient will use the proxy.
         */
        /*
         * Verify if the Bright data proxy is enabled.
         * If it is enabled, the HttpClient will use the proxy.
         */
        for (ProxyConfig config : proxyConfigs) {
            HttpHost brightDataProxy = new HttpHost(config.getHost(), config.getPort());
            if (config.hasValidCredentials()) {
                assert config.getUsername() != null;
                assert config.getPassword() != null;
                // CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                // UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(config.getUsername(), config.getPassword());
                // credentialsProvider.setCredentials(new AuthScope(brightDataProxy), credentials);
                // httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            // httpClientBuilder.setProxy(brightDataProxy);
        }

        /*
         * Build the HttpClient for the client with HttpClientBuilder.
         */
        httpClientBuilder.setDefaultCookieStore(this.cookieStore);
        httpClientBuilder.setConnectionManager(poolingClientConnectionManager);
        // httpClientBuilder.setMaxConnTotal(MAX_SYNCHRONOUS_REQUEST);
        // httpClientBuilder.setMaxConnPerRoute(MAX_SYNCHRONOUS_REQUEST);
        return (httpClientBuilder.build());
    }

    /*
     $      Request
     */

    public @NotNull CloseableHttpResponse get(@NotNull final GetRequest request) throws IOException {
        CloseableHttpClient httpClient = newHttpClient();
        HttpGet httpGet = new HttpGet(request.getUrl());

        return (httpClient.execute(httpGet));
    }

    public @NotNull RequestResponse post(@NotNull final PostRequest request) {
        return (new RequestResponse());
    }

}
