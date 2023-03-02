package com.br.dbc.captacao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioQuantidadePessoasInscritasPorNeurodiversidadeDTO {
    private String neurodiversidade;
    private Long quantidade;
}
