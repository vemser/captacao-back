package com.br.dbc.captacao.repository.enums;

public enum TipoTurno {
    MANHA("MANHA"),
    TARDE("TARDE"),
    NOITE("NOITE");

    private final String descricao;

    TipoTurno(String turno) {
        this.descricao = turno;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
