package com.br.dbc.captacao.entity;

import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.enums.TipoTurno;

import java.util.Set;

public class FormularioEntity {

    private Integer idFormulario;

    private TipoMarcacao matriculado;

    private String curso;

    private TipoTurno turno;

    private String instituicao;

    private String github;

    private String linkedin;

    private TipoMarcacao desafios;

    private TipoMarcacao problema;

    private TipoMarcacao reconhecimento;

    private TipoMarcacao altruismo;

    private String resposta;

    private TipoMarcacao lgpd;

    private TipoMarcacao prova;

    private String ingles;

    private String espanhol;

    private String neurodiversidade;

    private String configuracoes;

    private ImagemEntity imagemConfigPc;

    private TipoMarcacao efetivacao;

    private TipoMarcacao disponibilidade;

    private String genero;

    private String orientacao;

    private String importancia;

    private String inspiracao;

    private CandidatoEntity candidato;

    private Set<TrilhaEntity> trilhaEntitySet;
}
