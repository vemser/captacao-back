package com.br.dbc.captacao.dto.candidato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoNotaComportamentalDTO {

    private Double notaComportamental;
    private String parecerComportamental;
}