package com.br.dbc.captacao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioCandidatoPaginaPrincipalDTO {
    private Integer idCandidato;
    private String nomeCompleto;
    private String email;
    private Double notaProva;
    private String trilha;
    private String edicao;
}
