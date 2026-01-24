package example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Bean("backendRestTemplate")
    @Primary
    public RestTemplate restTemplate()  {
        return new RestTemplate();
    }
//    @Bean("loadBalancedRestTemplate")
//    @LoadBalanced
//    public RestTemplate loadBalancedRestTemplate() {
//        return new RestTemplate();
//    }
}

