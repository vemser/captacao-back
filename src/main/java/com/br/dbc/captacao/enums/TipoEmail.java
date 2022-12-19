package com.br.dbc.captacao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoEmail {
    CONFIRMACAO("Token para confirmar a entrevista"),
    REC_SENHA("Token para recuperação do e-mail realizada!");

    private final String descricao;

}
