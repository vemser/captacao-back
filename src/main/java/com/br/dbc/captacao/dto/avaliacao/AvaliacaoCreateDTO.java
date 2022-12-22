package com.br.dbc.captacao.dto.avaliacao;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoCreateDTO {


    @NotNull
    private boolean aprovadoBoolean;
    @NotNull
    private Integer idInscricao;

    private String emailGestor;
}