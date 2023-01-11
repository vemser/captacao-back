package com.br.dbc.captacao.repository.enums;

public enum Genero {
    M(0),
    F(1),
    O(2);

    private Integer genero;

    Genero(Integer genero) {
        this.genero = genero;
    }

    public Integer getGenero() {
        return genero;
    }
}
