package example;

import example.model.GrammyNomination;
import example.model.GrammyType;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GrammyService {

//    private final String BASE_URL = "https://helios.cs.ifmo.ru:10843/musicband-service/api/v1/bands";
//private final String BASE_URL = "http://localhost:10843/api/v1/bands";
//private final String BASE_URL = "http://backend/api/v1/bands";
    private final String BASE_URL = "http://localhost:10843/api/v1/bands";
    private final GrammyNominationDao nominationDao;
    private final RestTemplate backendRestTemplate;

    public GrammyService(GrammyNominationDao nominationDao,  @Qualifier("backendRestTemplate")RestTemplate backendRestTemplate) {
        this.nominationDao = nominationDao;
        this.backendRestTemplate = backendRestTemplate;
    }


    public String getBands(Integer page, Integer size, String sortBy, String filterName) {
        String url = BASE_URL + "?page=" + (page != null ? page : 0) +
                "&size=" + (size != null ? size : 10);
        if (sortBy != null) url += "&sortBy=" + sortBy;
        if (filterName != null) url += "&filterName=" + filterName;

        return backendRestTemplate.getForObject(url, String.class);
    }

    public String addBand(String bandJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(bandJson, headers);
        return backendRestTemplate.postForObject(BASE_URL, entity, String.class);
    }

    public String getBandById(int id) {
        try {
            return backendRestTemplate.getForObject(BASE_URL + "/" + id, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw e;
        }
    }


    public void deleteBandById(int id) {
        backendRestTemplate.delete(BASE_URL + "/" + id);
    }

    public void updateBand(long id, String bandJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(bandJson, headers);
        backendRestTemplate.put(BASE_URL + "/" + id, entity);
    }

    public String groupByGenre() {
        return backendRestTemplate.getForObject(BASE_URL + "/group-by-genre", String.class);
    }

    public String countByFrontman(String frontMan) {
        return backendRestTemplate.getForObject(BASE_URL + "/count-by-frontman?frontMan=" + frontMan, String.class);
    }

    public String searchByName(String prefix) {
        return backendRestTemplate.getForObject(BASE_URL + "/search-by-name?prefix=" + prefix, String.class);
    }

    public boolean nominateBand(int bandId, String genre) {
        GrammyNomination nomination = new GrammyNomination();
        nomination.setBandId(bandId);
        nomination.setGenre(genre);
        nomination.setType(GrammyType.NOMINATE);
        nominationDao.save(nomination);
        return true;
    }

    public boolean rewardBand(int bandId, String genre) {
        GrammyNomination reward = new GrammyNomination();
        reward.setBandId(bandId);
        reward.setGenre(genre);
        reward.setType(GrammyType.REWARD);
        nominationDao.save(reward);
        return true;
    }
}