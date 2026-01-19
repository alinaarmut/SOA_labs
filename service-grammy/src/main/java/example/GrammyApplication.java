package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
        org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration.class
})
@EnableDiscoveryClient
@EntityScan("example.model")
@EnableJpaRepositories("example")
public class GrammyApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrammyApplication.class, args);
    }

}