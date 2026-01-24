package org.example.backend.config;

import external.MusicBandBusinessRemote;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import javax.net.ssl.*;
@Configuration
public class EjbConfig {

    @Bean
    public MusicBandBusinessRemote musicBandBusinessRemote() {
        try {
            Properties props = new Properties();
            props.put("java.naming.factory.initial", "org.apache.openejb.client.RemoteInitialContextFactory");
            props.put("java.naming.provider.url", "http://localhost:8080/tomee/ejb");
//            props.put("java.naming.provider.url", "https://localhost:8443/tomee/ejb");
//            props.put(Context.SECURITY_PRINCIPAL, "remoteuser");
//            props.put(Context.SECURITY_CREDENTIALS, "remotepass");

            Context ctx = new InitialContext(props);
            MusicBandBusinessRemote ejb = (MusicBandBusinessRemote) ctx.lookup(
                    "global/ejb-1.0/MusicBandEjb!external.MusicBandBusinessRemote"
            );

            System.out.println("✅ Remote EJB подключён: " + ejb);
            return ejb;
        } catch (NamingException e) {
            System.err.println("❌ Ошибка Remote EJB lookup: " + e.getMessage());
            throw new RuntimeException("Не удалось подключиться к EJB", e);
        }
    }

}
