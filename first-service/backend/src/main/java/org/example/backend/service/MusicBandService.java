package org.example.backend.service;

import external.MusicBandBusinessRemote;
import org.example.backend.dto.MusicBandDto;
import org.example.model.MusicBand;
import org.example.model.MusicGenre;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MusicBandService {

    private final MusicBandBusinessRemote ejb;

    public MusicBandService(MusicBandBusinessRemote ejb) {
        this.ejb = ejb;
    }

    public List<MusicBand> getAll(int page, int size, String sortBy, String filterName) {
        if (filterName != null && !filterName.trim().isEmpty()) {
            return ejb.searchByNamePrefix(filterName.trim());
        }
        return ejb.getAll();
    }

    public MusicBand add(MusicBand band) {
        return ejb.add(band);
    }

    public MusicBandDto getById(Integer id) {
        MusicBand band = ejb.getById(id);
        if (band == null) return null;
        return convertToDto(band);
    }

    public boolean deleteById(Integer id) {
        return ejb.deleteById(id);
    }

    public List<MusicBand> searchByNamePrefix(String prefix) {
        return ejb.searchByNamePrefix(prefix);
    }

    public int countByFrontman(String frontMan) {
        return ejb.countByFrontman(frontMan);
    }

    public List<MusicBand> findByGenre(MusicGenre genre) {
        return ejb.findByGenre(genre);
    }

    public Map<String, Long> groupByGenre() {
        return ejb.groupByGenre();
    }

    public MusicBandDto update(long id, MusicBand updatedBand) {
        MusicBand band = ejb.update(id, updatedBand);
        return convertToDto(band);
    }

    private MusicBandDto convertToDto(MusicBand band) {
        MusicBandDto dto = new MusicBandDto();
        dto.setId(band.getId());
        dto.setName(band.getName());
        dto.setCreationDate(band.getCreationDate() != null ? band.getCreationDate().toString() : null);
        dto.setNumberOfParticipants(band.getNumberOfParticipants());
        dto.setGenre(band.getGenre());
        dto.setCoordinatesId(band.getCoordinates() != null ? band.getCoordinates().getId() : null);
        dto.setFrontManId(band.getFrontMan() != null ? band.getFrontMan().getId() : null);
        return dto;
    }
}
