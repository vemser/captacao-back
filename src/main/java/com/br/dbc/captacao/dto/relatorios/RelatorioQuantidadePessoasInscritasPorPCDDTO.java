package com.br.dbc.captacao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioQuantidadePessoasInscritasPorPCDDTO {
    private String pcd;
    private Long quantidade;
}
