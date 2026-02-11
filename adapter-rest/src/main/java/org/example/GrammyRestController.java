package org.example;

import org.example.client.GrammySoapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.soap.SOAPFaultException;

@RestController
@RequestMapping("/api/grammy")
public class GrammyRestController {

    @Autowired
    private GrammySoapClient soapClient;

    @GetMapping("/test")
    public String test() {
        return "rest-adapter на порту";
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("{\"status\":\"UP\"}");
    }

    @GetMapping
    public ResponseEntity<?> getBands(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String filterName
    ) {
        try {
            String result = soapClient.getBands(page, size, sortBy, filterName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> addBand(@RequestBody String bandJson) {
        try {
            String result = soapClient.addBand(bandJson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBandById(@PathVariable("id") int id) {
        try {
            String result = soapClient.getBandById(id);
            System.out.println("Result from service: " + result);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\":\"Band with id " + id + " not found\"}");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBand(
            @PathVariable("id") long id,
            @RequestBody String bandJson
    ) {
        try {
            soapClient.updateBand(id, bandJson);
            return ResponseEntity.ok("{\"message\":\"Band updated\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBand(@PathVariable("id") int id) {
        try {
            soapClient.deleteBand(id);
            return ResponseEntity.ok("{\"message\":\"Band deleted\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/group-by-genre")
    public ResponseEntity<?> groupByGenre() {
        try {
            String result = soapClient.groupByGenre();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/count-by-frontman")
    public ResponseEntity<?> countByFrontman(@RequestParam("frontMan") String frontMan) {
        try {
            String result = soapClient.countByFrontman(frontMan);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<?> searchByName(@RequestParam("prefix") String prefix) {
        try {
            String result = soapClient.searchByName(prefix);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/band/{bandId}/nominate/{genre}")
    public ResponseEntity<?> nominateBand(
            @PathVariable("bandId") int bandId,
            @PathVariable("genre") String genre
    ) {
        try {
            String result = soapClient.nominateBand(bandId, genre);
            return ResponseEntity.ok(result);
        }  catch (SOAPFaultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/band/{bandId}/reward/{genre}")
    public ResponseEntity<?> rewardBand(
            @PathVariable("bandId") int bandId,
            @PathVariable("genre") String genre
    ) {
        try {
            String result = soapClient.rewardBand(bandId, genre);
            return ResponseEntity.ok(result);

        } catch (SOAPFaultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
