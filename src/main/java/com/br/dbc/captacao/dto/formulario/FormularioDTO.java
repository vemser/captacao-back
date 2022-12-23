package com.br.dbc.captacao.dto.formulario;

import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.enums.TipoTurno;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormularioDTO {

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

    private Integer curriculo;

    private TipoMarcacao lgpd;

    private TipoMarcacao prova;

    private String ingles;

    private String espanhol;

    private String neurodiversidade;

    private String configuracoes;

    private TipoMarcacao efetivacao;

    private String genero;

    private String orientacao;

    private TipoMarcacao disponibilidade;

    private Set<TrilhaDTO> trilhas;

}