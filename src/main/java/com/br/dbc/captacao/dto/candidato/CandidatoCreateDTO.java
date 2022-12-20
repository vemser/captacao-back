package com.br.dbc.captacao.dto.candidato;

import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.EdicaoEntity;
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

    private Set<LinguagemDTO> linguagens;

    private TrilhaDTO trilha;

    private EntrevistaDTO entrevistaEntity;

    private EdicaoDTO edicao;


}
