package org.example.backend.dto;

import lombok.Data;
import org.example.model.MusicGenre;
import java.time.LocalDate;
public class MusicBandDto {
    private Long id;
    private String name;
    private String creationDate;
    private Integer numberOfParticipants;
    private MusicGenre genre;
    private Long coordinatesId;
    private Long frontManId;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCreationDate() { return creationDate; }
    public Integer getNumberOfParticipants() { return numberOfParticipants; }
    public MusicGenre getGenre() { return genre; }
    public Long getCoordinatesId() { return coordinatesId; }
    public Long getFrontManId() { return frontManId; }


    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }
    public void setNumberOfParticipants(Integer n) { this.numberOfParticipants = n; }
    public void setGenre(MusicGenre genre) { this.genre = genre; }
    public void setCoordinatesId(Long coordinatesId) { this.coordinatesId = coordinatesId; }
    public void setFrontManId(Long frontManId) { this.frontManId = frontManId; }
}

