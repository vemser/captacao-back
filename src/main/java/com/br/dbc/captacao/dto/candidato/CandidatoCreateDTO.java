package com.br.dbc.captacao.dto.candidato;

import com.br.dbc.captacao.entity.EntrevistaEntity;
import com.br.dbc.captacao.entity.LinguagemEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoCreateDTO {

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

    private String parecerComportamental;

    private String parecerTecnico;

    private Double media;

    private Set<LinguagemEntity> linguagens;

    private TrilhaEntity trilha;

    private EntrevistaEntity entrevistaEntity;

}
