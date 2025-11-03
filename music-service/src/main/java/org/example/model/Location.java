package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double x;
    private Integer y;
    private String name;
}
