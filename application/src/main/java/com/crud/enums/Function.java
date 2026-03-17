package com.crud.enums;

import com.crud.exceptions.FunctionNotFoundException;

public enum Function {
    DESIGNER("Designer"),
    ENGENHARIA("Engenharia"),
    PRODUTO("Produto"),
    OPERACOES("Operacoes");

    private final String descricao;

    Function(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Function fromName(String name) {
        try {
            return Function.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FunctionNotFoundException(name);
        }
    }
}
