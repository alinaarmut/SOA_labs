package org.example.backend;

import external.MusicBandBusinessRemote;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BackendApplication {

    public BackendApplication() {
        System.out.println(">>> BackendApplication CONSTRUCTOR"); // было
    }

    public static void main(String[] args) {
        System.out.println(">>> BackendApplication.main()");
        SpringApplication.run(BackendApplication.class, args);
    }

}
