package com.br.dbc.captacao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "vw_candidatos_ultima_edicao")
public class VwCandidatosUltimaEdicao {
    @Id
    @Column(name = "id_formulario")
    private Long idFormulario;

    @Column(name = "id_candidato")
    private Long idCandidato;

    @Column(name = "data_inscricao")
    private LocalDate dataInscricao;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "rg")
    private String rg;

    @Column(name = "estado")
    private String estado;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "instituicao")
    private String instituicao;

    @Column(name = "curso")
    private String curso;

    @Column(name = "turno")
    private String turno;

    @Column(name = "motivos_area_tech")
    private String motivosAreaTech;

    @Column(name = "prova")
    private String prova;

    @Column(name = "trilhas")
    private String trilhas;

    @Column(name = "disponibilidade")
    private String disponibilidade;

    @Column(name = "efetivacao")
    private String efetivacao;

    @Column(name = "alguem_ensinou_importante")
    private String alguemEnsinouImportante;

    @Column(name = "ingles")
    private String ingles;

    @Column(name = "espanhol")
    private String espanhol;

    @Column(name = "genero")
    private String genero;

    @Column(name = "orientacao")
    private String orientacao;

    @Column(name = "neurodiversidade")
    private String neurodiversidade;

    @Column(name = "pcd")
    private String pcd;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "github")
    private String github;

}
