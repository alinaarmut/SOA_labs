package org.example.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Error {
    private String message;

    public Error(String message) { this.message = message; }

}
