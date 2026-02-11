package example.rest;

import example.GrammyNominationDao;
import example.model.GrammyNomination;
import example.model.GrammyType;

import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

@Service
public class GrammyService {
    private final GrammyNominationDao nominationDao;
    private final com.example.ws.GrammyService mulePort;

    public GrammyService(GrammyNominationDao nominationDao, com.example.ws.GrammyService mulePort) {
        this.nominationDao = nominationDao;
        this.mulePort = mulePort;
    }


    public String getBands(Integer page, Integer size, String sortBy, String filterName) {
        return mulePort.getBands(page, size, sortBy, filterName);
    }

    public String addBand(String bandJson) {
        return mulePort.addBand(bandJson);
    }

    public String getBandById(int id) {
        return mulePort.getBandById(id);
    }


    public String deleteBandById(int id) {
        return mulePort.deleteBand(id);
    }

    public String updateBand(long id, String bandJson) {
        return mulePort.updateBand(id, bandJson);
    }

    public String groupByGenre() {
        return mulePort.groupByGenre();
    }

    public String countByFrontman(String frontMan) {
        return mulePort.countByFrontman(frontMan);
    }

    public String searchByName(String prefix) {
        return mulePort.searchByName(prefix);
    }

    public void assertBandExists(int bandId) {
        String response = mulePort.getBandById(bandId);
        if (response == null || response.isBlank()) {
            throw new NotFoundException("Band not found");
        }
        if (response.contains("\"error\"")) {
            throw new NotFoundException(response);
        }
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
