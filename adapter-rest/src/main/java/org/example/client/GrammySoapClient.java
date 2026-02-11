package org.example.client;

import org.example.adapter.soap.generated.*;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.net.URL;

@Component
public class GrammySoapClient {

    private final GrammyService port;

    public GrammySoapClient() {
        try {
            URL wsdlURL = new URL("http://localhost:8444/services/grammy?wsdl");
            QName SERVICE_NAME = new QName("http://ws.example.org/", "GrammyWebService");

            GrammyWebService service = new GrammyWebService(wsdlURL, SERVICE_NAME);
            this.port = service.getGrammyServicePort();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SOAP client", e);
        }
    }

    public String getBandById(int id) {
        return port.getBandById(id);
    }

    public String groupByGenre() {
        return port.groupByGenre();
    }

    public String addBand(String bandJson) {
        return port.addBand(bandJson);
    }

    public String updateBand(long id, String bandJson) {
        return port.updateBand(id, bandJson);
    }

    public String deleteBand(int id) {
        return port.deleteBand(id);
    }

    public String getBands(Integer page, Integer size, String sortBy, String filterName) {
        return port.getBands(page, size, sortBy, filterName);
    }

    public String countByFrontman(String frontMan) {
        return port.countByFrontman(frontMan);
    }

    public String searchByName(String prefix) {
        return port.searchByName(prefix);
    }

    public String nominateBand(int bandId, String genre) {
        return port.nominateBand(bandId, genre);
    }

    public String rewardBand(int bandId, String genre) {
        return port.rewardBand(bandId, genre);
    }
}
