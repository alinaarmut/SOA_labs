package org.example.service;

import org.example.model.MusicBand;
import org.example.model.MusicGenre;
import org.example.model.repository.MusicBandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MusicBandService {

//    @Autowired
//    private MusicBandRepository repository;
private final MusicBandRepository repository;
public MusicBandService(MusicBandRepository repository) {
    this.repository = repository;
    System.out.println("==== MusicBandService CREATED ====");
    System.out.println("Repository: " + (repository != null ? repository.getClass().getName() : "NULL"));
}

    public List<MusicBand> getAll(int page, int size, String sortBy, String filterName) {
        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            switch (sortBy) {
                case "name": sort = Sort.by("name"); break;
                case "genre": sort = Sort.by("genre"); break;
                case "date": sort = Sort.by("creationDate"); break;
                case "participants": sort = Sort.by("numberOfParticipants"); break;
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        if (filterName != null && !filterName.isEmpty()) {
            return repository.findByNameContainingIgnoreCase(filterName);
        }

        return repository.findAll(pageable).getContent();
    }

    public MusicBand add(MusicBand band) {
        if (band.getCreationDate() == null) {
            band.setCreationDate(LocalDate.now());
        }

        // Валидация
        if (band.getName() == null || band.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название группы обязательно");
        }
        if (band.getCoordinates() == null) {
            throw new IllegalArgumentException("Координаты обязательны");
        }
        if (band.getNumberOfParticipants() != null && band.getNumberOfParticipants() <= 0) {
            throw new IllegalArgumentException("Количество участников должно быть больше 0");
        }

        return repository.save(band);
    }

    public MusicBand getById(Integer id) {
        return repository.findById(id.longValue()).orElse(null);
    }

    public boolean deleteById(Integer id) {
        if (repository.existsById(id.longValue())) {
            repository.deleteById(id.longValue());
            return true;
        }
        return false;
    }

    public MusicBand update(long id, MusicBand updatedBand) {
        MusicBand existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Группа с id=" + id + " не найдена"));

        // Обновление полей
        existing.setName(updatedBand.getName());
        existing.setCoordinates(updatedBand.getCoordinates());
        existing.setNumberOfParticipants(updatedBand.getNumberOfParticipants());
        existing.setGenre(updatedBand.getGenre());
        existing.setFrontMan(updatedBand.getFrontMan());

        return repository.save(existing);
    }

    public Map<String, Long> groupByGenre() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getGenre() != null ? b.getGenre().name() : "Не указан",
                        Collectors.counting()
                ));
    }

    public int countByFrontman(String frontMan) {
        return (int) repository.countByFrontManName(frontMan);
    }

    public List<MusicBand> searchByNamePrefix(String prefix) {
        return repository.findByNameStartingWithIgnoreCase(prefix);
    }

    // Дополнительные методы
    public List<MusicBand> findByGenre(MusicGenre genre) {
        return repository.findByGenre(genre);
    }
}
