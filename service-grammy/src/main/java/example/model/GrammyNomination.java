package example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


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
    @Column(name = "type", columnDefinition = "grammy_type")
    private GrammyType type;
}
