package com.br.dbc.captacao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioQuantidadePessoasInscritasPorEdicaoDTO {
    private String edicao;
    private Long quantidade;
}
