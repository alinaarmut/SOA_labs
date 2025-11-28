package example;

import example.model.GrammyNomination;
import example.model.GrammyType;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Stateless
public class GrammyService {
    private final Client client;
    private final String BASE_URL = "https://helios.cs.ifmo.ru:10843/musicband-service/api/v1/bands";
    @Inject
    private GrammyNominationDao nominationDao;

    public GrammyService() {
        try {
            // Создаём SSL Context для доверия самоподписанным сертификатам
            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            sslContext.init(null, trustAllCerts, new SecureRandom());


            this.client = ClientBuilder.newBuilder()
                    .sslContext(sslContext)
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize HTTPS client", e);
        }
    }


    public Response getBands(Integer page, Integer size, String sortBy, String filterName) {
        String url = BASE_URL + "?page=" + (page != null ? page : 0) +
                "&size=" + (size != null ? size : 10);
        if (sortBy != null) url += "&sortBy=" + sortBy;
        if (filterName != null) url += "&filterName=" + filterName;
        System.out.println("DEBUG GrammyService: target url = " + url);
        return client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public Response addBand(String bandJson) {
        return client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(bandJson));
    }

    public Response getBandById(int id) {
        return client.target(BASE_URL + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public Response deleteBandById(int id) {
        return client.target(BASE_URL + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .delete();
    }

    public Response updateBand(long id, String bandJson) {
        return client.target(BASE_URL + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(bandJson));
    }

    public Response groupByGenre() {
        return client.target(BASE_URL + "/group-by-genre")
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public Response countByFrontman(String frontMan) {
        return client.target(BASE_URL + "/count-by-frontman?frontMan=" + frontMan)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public Response searchByName(String prefix) {
        return client.target(BASE_URL + "/search-by-name?prefix=" + prefix)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public boolean nominateBand(int bandId, String genre) {
        Response bandResponse = getBandById(bandId);
        if (bandResponse.getStatus() != 200) return false;

        GrammyNomination nomination = new GrammyNomination();
        nomination.setBandId(bandId);
        nomination.setGenre(genre);
        nomination.setType(GrammyType.NOMINATE);
        System.out.println("bandId: " + nomination.getBandId());

        nominationDao.save(nomination);

        return true;
    }


    public boolean rewardBand(int bandId, String genre) {
        GrammyNomination nomination = new GrammyNomination();
        nomination.setBandId(bandId);
        nomination.setGenre(genre);
        nomination.setType(GrammyType.REWARD);
        nominationDao.save(nomination);
        return true;
    }
}

