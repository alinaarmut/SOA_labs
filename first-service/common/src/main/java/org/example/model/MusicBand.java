package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Data
@Table(name = "music_bands")
public class MusicBand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates;

    private LocalDate creationDate;
    private Integer numberOfParticipants;
    @Enumerated(EnumType.STRING)
    private MusicGenre genre;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "front_man_id")
    private Person frontMan;


}
