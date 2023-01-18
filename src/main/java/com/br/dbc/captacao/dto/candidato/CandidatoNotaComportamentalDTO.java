package com.br.dbc.captacao.dto.candidato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoNotaComportamentalDTO {

    @Min(0)
    @Max(100)
    @NotNull
    private Double notaComportamental;
    @NotBlank
    private String parecerComportamental;
}