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

    @Min(value = 0,  message = "Nota deve ser entre 0 e 100")
    @Max(value = 100, message = "Nota deve ser entre 0 e 100")
    private Double notaProva;
}
