package com.br.dbc.captacao.entity;

import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.enums.TipoTurno;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "FORMULARIO")
public class FormularioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FORMULARIO")
    @SequenceGenerator(name = "SEQ_FORMULARIO", sequenceName = "SEQ_FORMULARIO", allocationSize = 1)
    @Column(name = "id_formulario")
    private Integer idFormulario;

    @Column(name = "MATRICULA")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao matriculado;

    @Column(name = "CURSO")
    private String curso;

    @Column(name = "TURNO")
    @Enumerated(EnumType.ORDINAL)
    private TipoTurno turno;

    @Column(name = "INSTITUICAO")
    private String instituicao;

    @Column(name = "GITHUB")
    private String github;

    @Column(name = "LINKEDIN")
    private String linkedin;

    @Column(name = "DESAFIOS")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao desafios;

    @Column(name = "PROBLEMAS")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao problema;

    @Column(name = "RECONHECIMENTO")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao reconhecimento;

    @Column(name = "ALTRUISMO")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao altruismo;

    @Column(name = "RESPOSTA")
    private String resposta;

    @Column(name = "LGPD")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao lgpd;

    @Column(name = "PROVA")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao prova;

    @Column(name = "INGLES")
    private String ingles;

    @Column(name = "ESPANHOL")
    private String espanhol;

    @Column(name = "NEURODIVERSIDADE")
    private String neurodiversidade;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRINT_CONFIG_PC", referencedColumnName = "ID_PRINT_CONFIG_PC")
    private PrintConfigPCEntity imagemConfigPc;

    @Column(name = "EFETIVACAO")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao efetivacao;

    @Column(name = "DISPONIBILIDADE")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao disponibilidade;

    @Column(name = "GENERO")
    private String genero;

    @Column(name = "ORIENTACAO")
    private String orientacao;

    @Column(name = "IMPORTANCIA_TI")
    private String importancia;

    @JsonIgnore
    @OneToOne(mappedBy = "formularioEntity", fetch = FetchType.LAZY)
    private CandidatoEntity candidato;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TRILHA_FORM",
            joinColumns = @JoinColumn(name = "id_formulario"),
            inverseJoinColumns = @JoinColumn(name = "id_trilha")
    )
    private Set<TrilhaEntity> trilhaEntitySet;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURRICULO", referencedColumnName = "ID_CURRICULO")
    private CurriculoEntity curriculoEntity;
}
