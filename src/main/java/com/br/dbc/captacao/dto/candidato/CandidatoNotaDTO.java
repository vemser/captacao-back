package com.br.dbc.captacao.dto.candidato;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoNotaDTO {

    @Min(0)
    @Max(100)
    private Double notaProva;
}
