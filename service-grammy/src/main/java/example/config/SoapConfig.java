package example.config;


import example.GrammySoapService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;
@Configuration
public class SoapConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private GrammySoapService grammySoapService;

    @Bean
    public Endpoint grammyEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, grammySoapService);
        endpoint.publish("/grammy");
        return endpoint;
    }
}