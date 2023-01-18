package com.br.dbc.captacao.dto.candidato;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class CandidatoTecnicoNotaDTO {

    @Min(0)
    @Max(100)
    @NotNull
    private Double notaTecnica;
    @NotBlank
    private String parecerTecnico;
}
