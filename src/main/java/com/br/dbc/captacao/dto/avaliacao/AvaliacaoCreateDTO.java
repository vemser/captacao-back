package com.br.dbc.captacao.dto.avaliacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoCreateDTO {

    @NotNull
    private boolean aprovadoBoolean;
    @NotNull
    private Integer idInscricao;

}