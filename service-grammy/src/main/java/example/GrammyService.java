package example;

import example.model.GrammyNomination;
import example.model.GrammyType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GrammyService {

//    private final String BASE_URL = "https://helios.cs.ifmo.ru:10843/musicband-service/api/v1/bands";
private final String BASE_URL = "http://localhost:10843/api/v1/bands";// для load balancer

    private final GrammyNominationDao nominationDao;
    private final RestTemplate restTemplate;

    public GrammyService(GrammyNominationDao nominationDao, RestTemplate restTemplate) {
        this.nominationDao = nominationDao;
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<?> getBands(Integer page, Integer size, String sortBy, String filterName) {
        String url = BASE_URL + "?page=" + (page != null ? page : 0) +
                "&size=" + (size != null ? size : 10);
        if (sortBy != null) url += "&sortBy=" + sortBy;
        if (filterName != null) url += "&filterName=" + filterName;
        System.out.println("DEBUG GrammyService: target url = " + url);
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> addBand(String bandJson) {
        String response = restTemplate.postForObject(BASE_URL, bandJson, String.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getBandById(int id) {
        String response = restTemplate.getForObject(BASE_URL + "/" + id, String.class);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\":\"Band with id " + id + " not found\"}");
        }
    }

    public ResponseEntity<Void> deleteBandById(int id) {
        restTemplate.delete(BASE_URL + "/" + id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> updateBand(long id, String bandJson) {
        restTemplate.put(BASE_URL + "/" + id, bandJson);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> groupByGenre() {
        String response = restTemplate.getForObject(BASE_URL + "/group-by-genre", String.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> countByFrontman(String frontMan) {
        String response = restTemplate.getForObject(BASE_URL + "/count-by-frontman?frontMan=" + frontMan, String.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> searchByName(String prefix) {
        String response = restTemplate.getForObject(BASE_URL + "/search-by-name?prefix=" + prefix, String.class);
        return ResponseEntity.ok(response);
    }

    public boolean nominateBand(int bandId, String genre) {
        ResponseEntity<?> bandResponse = getBandById(bandId);
        if (bandResponse.getStatusCode().isError()) return false;

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

