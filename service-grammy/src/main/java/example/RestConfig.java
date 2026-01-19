//package example;
//
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
//import org.apache.hc.client5.http.io.HttpClientConnectionManager;
//import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
//import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
//import org.apache.hc.core5.ssl.SSLContexts;
//import org.apache.http.client.HttpClient;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//import javax.net.ssl.SSLContext;
//@Configuration
//public class RestConfig {
//    @Bean
//    @LoadBalanced // балансировщик нагрузки
//    public RestTemplate restTemplate() throws Exception {
//        SSLContext sslContext = SSLContexts.custom()
//                .loadTrustMaterial(null, (chain, authType) -> true) // доверяем всем (только для dev!)
//                .build();
//
//        var sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
//                .setSslContext(sslContext)
//                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE) // отключаем проверку hostname
//                .build();
//
//        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
//                .setSSLSocketFactory(sslSocketFactory)
//                .build();
//
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(cm)
//                .build();
//
//        HttpComponentsClientHttpRequestFactory requestFactory =
//                new HttpComponentsClientHttpRequestFactory((HttpClient) httpClient);
//
//        return new RestTemplate(requestFactory);
//    }
//
//    }
package example;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class RestConfig {

    @Bean("backendRestTemplate")
    @Primary
    public RestTemplate restTemplate() throws Exception {
        // SSL context: доверяем всем (dev only)
        TrustStrategy acceptingTrustStrategy = (chain, authType) -> true;

        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                NoopHostnameVerifier.INSTANCE
        );

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory((HttpClient) httpClient);

        return new RestTemplate(requestFactory);
    }
    @Bean("loadBalancedRestTemplate")
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }
}

