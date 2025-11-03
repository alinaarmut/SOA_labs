package org.example.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.model.MusicBand;
import org.example.service.MusicBandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/bands")
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"})
public class MusicBandController {
    @Autowired
    private MusicBandService service;

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
                    .body(Map.of("error", "Разработчик ушёл слушать джаз вместо обработки запроса :)"));
        }
    }



    @PostMapping
    public ResponseEntity<?> addBand(@Valid @RequestBody MusicBand band) {
        try {
            log.info("Получен запрос на создание группы: {}", band.getName());
            MusicBand created = service.add(band);
            log.info("Группа успешно создана с ID: {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка валидации при создании группы: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Внутренняя ошибка сервера при создании группы: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Внутренняя ошибка сервера",
                            "message", e.getMessage(),
                            "type", e.getClass().getSimpleName()
                    ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBandById(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Некорректный id группы"));
            }
            MusicBand band = service.getById(id);
            if (band != null) {
                return ResponseEntity.ok(band);
            }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Группа с id=" + id + " не найдена"));
    } catch(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Разработчик случайно сыграл b вместо id :)"));
    }
}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
        boolean deleted = service.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Группа с id=" + id + " не найдена"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Разработчик удалил гитару вместо группы :)"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBand(@PathVariable long id, @Valid @RequestBody MusicBand updatedBand) {
        try {
            MusicBand result = service.update(id, updatedBand);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Внутренняя ошибка сервера"));
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
}