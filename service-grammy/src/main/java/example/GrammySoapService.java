package example;

import example.rest.GrammyService;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.soap.SOAPFaultException;
import javax.ws.rs.NotFoundException;

@WebService(
        name = "GrammyService",
        serviceName = "GrammyWebService",
        portName = "GrammyServicePort",
        targetNamespace = "http://ws.example.org/",
        wsdlLocation = "classpath:wsdl/grammy.wsdl"
)
@Service
public class GrammySoapService {

    private final GrammyService grammyService;

    public GrammySoapService(GrammyService grammyService) {
        this.grammyService = grammyService;
    }


    private static SOAPFaultException soapFault(String message) {
        try {
            return new SOAPFaultException(
                    SOAPFactory.newInstance().createFault(
                            message,
                            new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, "Client")
                    )
            );
        } catch (SOAPException e) {
            throw new RuntimeException("Failed to create SOAP fault", e);
        }
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String getBandById(@WebParam(name = "id", targetNamespace = "") int id) {
        return grammyService.getBandById(id);
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String getBands(
            @WebParam(name = "page", targetNamespace = "") Integer page,
            @WebParam(name = "size", targetNamespace = "") Integer size,
            @WebParam(name = "sortBy", targetNamespace = "") String sortBy,
            @WebParam(name = "filterName", targetNamespace = "") String filterName
    ) {
        return grammyService.getBands(page, size, sortBy, filterName);
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String addBand(@WebParam(name = "bandJson", targetNamespace = "") String bandJson) {
        return grammyService.addBand(bandJson);
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String updateBand(
            @WebParam(name = "id", targetNamespace = "") long id,
            @WebParam(name = "bandJson", targetNamespace = "") String bandJson
    ) {
        String result = grammyService.updateBand(id, bandJson);
        return result != null ? result : "{\"message\":\"Band updated\"}";
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String deleteBand(@WebParam(name = "id", targetNamespace = "") int id) {
        String result = grammyService.deleteBandById(id);
        return result != null ? result : "{\"message\":\"Band deleted\"}";
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String groupByGenre() {
        return grammyService.groupByGenre();
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String countByFrontman(@WebParam(name = "frontMan", targetNamespace = "") String frontMan) {
        return grammyService.countByFrontman(frontMan);
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String searchByName(@WebParam(name = "prefix", targetNamespace = "") String prefix) {
        return grammyService.searchByName(prefix);
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String nominateBand(
            @WebParam(name = "bandId", targetNamespace = "") int bandId,
            @WebParam(name = "genre", targetNamespace = "") String genre
    ) {
        try {
            grammyService.assertBandExists(bandId);
        } catch (NotFoundException e) {
            throw soapFault("Band not found: id=" + bandId);
        }
        return String.valueOf(grammyService.nominateBand(bandId, genre));
    }

    @WebMethod
    @WebResult(name = "return", targetNamespace = "")
    public String rewardBand(
            @WebParam(name = "bandId", targetNamespace = "") int bandId,
            @WebParam(name = "genre", targetNamespace = "") String genre
    ) {
        try {
            grammyService.assertBandExists(bandId);
        } catch (NotFoundException e) {
            throw soapFault("Band not found: id=" + bandId);
        }
        return String.valueOf(grammyService.rewardBand(bandId, genre));
    }
}
