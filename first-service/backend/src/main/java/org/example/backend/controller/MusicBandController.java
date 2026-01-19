package org.example.backend.controller;

import external.MusicBandBusinessRemote;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dto.MusicBandDto;
import org.example.backend.service.MusicBandService;
import org.example.model.MusicBand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bands")
public class MusicBandController {

    @Autowired
    private MusicBandService service;

    @Autowired
    private MusicBandBusinessRemote ejb;

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> status = new HashMap<>();
        try {
            status.put("status", "UP");
            status.put("ejbTest", ejb.test("health-check"));
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
        }
        return status;
    }
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
        System.out.println(">>> /hello called, ejb=" + (ejb != null));
        return ejb != null ? ejb.test(name) : "EJB недоступен";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBandById(@PathVariable("id") Integer id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Некорректный id группы"));
            }
            MusicBandDto band = service.getById(id);
            if (band != null) {
                return ResponseEntity.ok(band);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Группа с id=" + id + " не найдена"));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Внутренняя ошибка сервера"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        try {
            boolean deleted = service.deleteById(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Группа с id=" + id + " не найдена"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<?> searchByName(@RequestParam String prefix) {
        try {
            List<MusicBand> bands = service.searchByNamePrefix(prefix);
            return ResponseEntity.ok(bands);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/count-by-frontman")
    public ResponseEntity<?> countByFrontman(@RequestParam String frontMan) {
        try {
            int count = service.countByFrontman(frontMan);
            return ResponseEntity.ok(Map.of("count", count, "frontMan", frontMan));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/group-by-genre")
    public ResponseEntity<?> groupByGenre() {
        try {
            Map<String, Long> grouped = service.groupByGenre();
            return ResponseEntity.ok(grouped);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getBands(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "filterName", required = false) String filterName
    ) {
        try {
            if (page < 0 || size <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Некорректные параметры page или size"));
            }
            List<MusicBand> bands = service.getAll(page, size, sortBy, filterName);
            return ResponseEntity.ok(bands);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addBand(@Valid @RequestBody MusicBand band) {
        try {
            MusicBand created = service.add(band);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBand(@PathVariable long id, @Valid @RequestBody MusicBand updatedBand) {
        try {
            MusicBandDto result = service.update(id, updatedBand);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}