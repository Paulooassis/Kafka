package com.crud.exceptions;


public class PositionNotFoundException extends RuntimeException {

    public PositionNotFoundException(String position) {
        super("Cargo "+ position + " não é uma opção válida");
    }
}
