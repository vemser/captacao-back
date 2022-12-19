package com.br.dbc.captacao.entity;

import com.br.dbc.captacao.enums.TipoMarcacao;

import java.time.LocalDate;

public class CandidatoEntity {

        private Integer idCandidato;

        private String nome;

        private LocalDate dataNascimento;

        private String email;

        private String telefone;

        private String rg;

        private String cpf;

        private String estado;

        private String cidade;

        private TipoMarcacao pcd;

        private String observacoes;

        private Double notaProva;

        private Double notaEntrevistaComportamental;

        private Double notaEntrevistaTecnica;

        private TipoMarcacao ativo;
}
