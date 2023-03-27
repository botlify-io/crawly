package io.botlify.websession;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.time.Instant;

public class TestClass {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        //CloseableHttpClient chc = HttpClientBuilder.create()
        //        .build();

        //HttpGet httpGet = new HttpGet("https://httpbin.org/get");

        //HttpResponse<String> response = chc.execute(null, httpGet, null,
        //System.out.println(test.statusCode());
        //System.out.println(test.body());

        try {
            for (int i = 0; i < 10; i++) {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


                Instant start = Instant.now();
                URL url = new URL("https://pro.permisdeconduire.gouv.fr/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // open the connection

                connection.setRequestMethod("GET"); // set the HTTP method
                connection.connect(); // connect to the server

                // read the response from the server
                int responseCode = connection.getResponseCode();
                // Read the body of the response as string
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                    content.append(inputLine);
                in.close();


                System.out.println("Response code: " + responseCode);
                System.out.println("Response message: " + content);

                connection.disconnect(); // disconnect from the server
                Instant end = Instant.now();
                System.out.println("Time: " + (end.toEpochMilli() - start.toEpochMilli()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
