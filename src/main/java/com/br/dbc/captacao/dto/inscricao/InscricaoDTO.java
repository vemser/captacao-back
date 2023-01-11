package com.br.dbc.captacao.dto.inscricao;

import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.repository.enums.TipoMarcacao;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InscricaoDTO {

    private Integer idInscricao;

    private CandidatoDTO candidato;

    private LocalDate dataInscricao;

    private TipoMarcacao avaliado;


}
