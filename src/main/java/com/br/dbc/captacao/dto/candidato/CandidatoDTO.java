package com.br.dbc.captacao.dto.candidato;

import com.br.dbc.captacao.dto.candidato.CandidatoCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoDTO extends CandidatoCreateDTO {

    private Integer idCandidato;

}
