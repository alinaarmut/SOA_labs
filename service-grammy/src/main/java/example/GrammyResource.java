package example;

import javax.ws.rs.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grammy")
public class GrammyResource {

    private final GrammyService service;

    public GrammyResource(GrammyService service) {
        this.service = service;
    }


    @GetMapping("/bands")
    public ResponseEntity<?> getBands(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String filterName
    ) {
        return service.getBands(page, size, sortBy, filterName);
    }

    @PostMapping("/bands")
    public ResponseEntity<?> addBand(@RequestBody String bandJson) {
        return service.addBand(bandJson);
    }

    @GetMapping("/bands/{id}")
    public ResponseEntity<?> getBandById(@PathVariable int id) {
        return service.getBandById(id);
    }

    @PutMapping("/bands/{id}")
    public ResponseEntity<?> updateBand(
            @PathVariable long id,
            @RequestBody String bandJson
    ) {
        return service.updateBand(id, bandJson);
    }

    @DeleteMapping("/bands/{id}")
    public ResponseEntity<?> deleteBand(@PathVariable int id) {
        return service.deleteBandById(id);
    }

    @GetMapping("/bands/group-by-genre")
    public ResponseEntity<?> groupByGenre() {
        return service.groupByGenre();
    }

    @GetMapping("/bands/count-by-frontman")
    public ResponseEntity<?> countByFrontman(@RequestParam String frontMan) {
        return service.countByFrontman(frontMan);
    }

    @GetMapping("/bands/search-by-name")
    public ResponseEntity<?> searchByName(@RequestParam String prefix) {
        return service.searchByName(prefix);
    }


    @PostMapping("/{bandId}/nominate/{genre}")
    public ResponseEntity<?> nominateBand(
            @PathParam("band-id") int bandId,
            @PathParam("genre") String genre
    ) {
        try {
            ResponseEntity<?> bandResponse = service.getBandById(bandId);
            if (bandResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\":\"Band with id " + bandId + " not found!\"}");
            }

            boolean ok = service.nominateBand(bandId, genre);
            if (ok) return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Band nominated\"}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Failed to nominate band\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Разработчик ушел играть на гитаре!\"}");
        }
    }


    public ResponseEntity<?> rewardBand(
            @PathParam("band-id") int bandId,
            @PathParam("genre") String genre
    ) {
        try {
            ResponseEntity<?> bandResponse = service.getBandById(bandId);
            if (bandResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\":\"Band with id " + bandId + " not found!\"}");
            }

            boolean ok = service.rewardBand(bandId, genre);
            if (ok) return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Band rewarded\"}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Failed to reward band\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Разработчик ушел играть на гитаре!\"}");
        }
    }
}
