package com.br.dbc.captacao.dto.candidato;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
public class CandidatoTecnicoNotaDTO {

    @Min(0)
    @Max(100)
    private Double notaTecnico;
    @NotBlank
    private String parecerTecnico;
}
