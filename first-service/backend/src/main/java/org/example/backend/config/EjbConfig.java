package org.example.backend.config;

import external.MusicBandBusinessRemote;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

import external.MusicBandBusinessRemote;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Properties;

@Configuration
public class EjbConfig {

    @Bean
    public MusicBandBusinessRemote musicBandBusinessRemote() {
        try {
            // ТОЛЬКО ДЛЯ РАЗРАБОТКИ! Отключаем проверку SSL
            disableSSLValidation();

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

    // ТОЛЬКО ДЛЯ РАЗРАБОТКИ!
    private void disableSSLValidation() {
        try {
            // Создаём TrustManager, который доверяет всем сертификатам
            TrustManager[] trustAllCerts = new TrustManager[]{
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

            // Устанавливаем SSLContext с нашим TrustManager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Отключаем проверку hostname
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            System.out.println("⚠️ SSL validation disabled (DEVELOPMENT ONLY)");
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Не удалось отключить SSL validation", e);
        }
    }
}
