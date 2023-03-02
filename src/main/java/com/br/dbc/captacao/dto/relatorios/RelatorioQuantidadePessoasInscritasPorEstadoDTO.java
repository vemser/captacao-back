package com.br.dbc.captacao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioQuantidadePessoasInscritasPorEstadoDTO {
    private String estado;
    private Long quantidade;
}
