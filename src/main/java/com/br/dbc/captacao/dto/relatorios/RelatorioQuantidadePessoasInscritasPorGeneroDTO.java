package com.br.dbc.captacao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioQuantidadePessoasInscritasPorGeneroDTO {
    private String genero;
    private Long quantidade;
}
