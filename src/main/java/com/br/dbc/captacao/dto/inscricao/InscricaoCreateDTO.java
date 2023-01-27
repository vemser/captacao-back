package com.br.dbc.captacao.dto.inscricao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscricaoCreateDTO {


    @Schema(example = "5")
    private Integer idCandidato;

}