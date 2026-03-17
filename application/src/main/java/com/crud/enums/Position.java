package com.crud.enums;

import com.crud.exceptions.PositionNotFoundException;

public enum Position {
    ESTAGIARIO("Estagiario"),
    JUNIOR("Junior"),
    PLENO("Pleno"),
    SENIOR("Senior");

    private String description;

    Position(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Position fromName(String name) {
        try {
            return Position.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PositionNotFoundException(name);
        }
    }
}