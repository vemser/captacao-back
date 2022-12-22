package com.br.dbc.captacao.dto.inscricao;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscricaoCreateDTO {


    @Schema(example = "5")
    private Integer idCandidato;

}