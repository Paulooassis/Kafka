package com.crud.exceptions;

public class FunctionNotFoundException extends RuntimeException {

    public FunctionNotFoundException(String function) {
        super("Função "+ function + " não é uma opção válida");
    }
}
