package example.config;

import com.example.ws.GrammyService;
import com.example.ws.GrammyWebService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class MuleSoapClientConfig {

    @Bean
    public GrammyService muleGrammyPort(
            @Value("${grammy.mule.soap-address:http://localhost:8082/integration}") String soapAddress
    ) throws MalformedURLException {
        URL wsdlUrl = MuleSoapClientConfig.class.getResource("/wsdl/grammy.wsdl");
        if (wsdlUrl == null) {
            throw new MalformedURLException("плаки-плаки: wsdl не найден по пути /wsdl/grammy.wsdl");
        }
        GrammyWebService service = new GrammyWebService(wsdlUrl);
        GrammyService port = service.getGrammyServicePort();
        ((BindingProvider) port).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, soapAddress);
        return port;
    }
}
