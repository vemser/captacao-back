package com.br.dbc.captacao.dto;

import com.br.dbc.captacao.enums.TipoMarcacao;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InscricaoDTO {

    private Integer idInscricao;

    private CandidatoDTO candidato;

    private LocalDate dataInscricao;

    private TipoMarcacao avaliacao;


}
