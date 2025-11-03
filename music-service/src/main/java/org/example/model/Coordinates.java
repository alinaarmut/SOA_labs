package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Double y;
}