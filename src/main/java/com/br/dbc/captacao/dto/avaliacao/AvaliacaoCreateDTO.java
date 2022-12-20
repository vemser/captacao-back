package com.br.dbc.captacao.dto.avaliacao;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoCreateDTO {

    private boolean aprovadoBoolean;

    @NotNull
    private Integer idInscricao;
}