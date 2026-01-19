package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
public class Location  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double x;
    private Integer y;
    private String name;

    public Long getId() {
        return id;
    }

    public Double getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Location() {}
}
