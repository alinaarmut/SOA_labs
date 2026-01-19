package example.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;


@Entity
@Table(name = "grammy_nomination")
@Getter
@Setter
public class GrammyNomination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String genre;
    @Column(name = "band_id")
    private int bandId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private GrammyType type;

    public Long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public int getBandId() {
        return bandId;
    }

    public GrammyType getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setBandId(int bandId) {
        this.bandId = bandId;
    }

    public void setType(GrammyType type) {
        this.type = type;
    }

    public GrammyNomination(Long id, String genre, int bandId, GrammyType type) {
        this.id = id;
        this.genre = genre;
        this.bandId = bandId;
        this.type = type;
    }

    public GrammyNomination() {
    }
}
