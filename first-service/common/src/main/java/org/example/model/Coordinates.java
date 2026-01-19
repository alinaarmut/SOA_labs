package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "coordinates")
public class Coordinates implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Double y;

    public Long getId() {
        return id;
    }

    public Integer getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }
    public Coordinates() {}
}