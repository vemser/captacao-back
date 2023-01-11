package com.br.dbc.captacao.entity;

import com.br.dbc.captacao.repository.enums.TipoMarcacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "CANDIDATO")
public class CandidatoEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CANDIDATO")
        @SequenceGenerator(name = "SEQ_CANDIDATO", sequenceName = "SEQ_CANDIDATO", allocationSize = 1)
        @Column(name = "id_candidato")
        private Integer idCandidato;

        @Column(name = "id_edicao", insertable = false, updatable = false)
        private Integer idEdicao;

        @Column(name = "nome")
        private String nome;

        @Column(name = "data_nascimento")
        private LocalDate dataNascimento;

        @Column(name = "email")
        private String email;

        @Column(name = "telefone")
        private String telefone;

        @Column(name = "rg")
        private String rg;

        @Column(name = "cpf")
        private String cpf;

        @Column(name = "estado")
        private String estado;

        @Column(name = "cidade")
        private String cidade;

        @Column(name = "pcd")
        private TipoMarcacao pcd;

        @Column(name = "observacoes")
        private String observacoes;

        @Column(name = "nota_prova")
        private Double notaProva;

        @Column(name = "nota_comportamental")
        private Double notaEntrevistaComportamental;

        @Column(name = "nota_tecnica")
        private Double notaEntrevistaTecnica;

        @Column(name = "ativo")
        private TipoMarcacao ativo;

        @Column(name = "parecer_comp")
        private String parecerComportamental;

        @Column(name = "parecer_tecnico")
        private String parecerTecnico;

        @Column(name = "media")
        private Double media;

        @JsonIgnore
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "LINGUAGEM_CANDIDATO",
                joinColumns = @JoinColumn(name = "ID_CANDIDATO"),
                inverseJoinColumns = @JoinColumn(name = "ID_LINGUAGEM"))
        private Set<LinguagemEntity> linguagens;

        @OneToOne(mappedBy = "candidatoEntity")
        private EntrevistaEntity entrevistaEntity;

        @OneToOne(mappedBy = "candidato", orphanRemoval = true)
        private ImagemEntity imageEntity;

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ID_EDICAO", referencedColumnName = "ID_EDICAO")
        @ToString.Exclude
        private EdicaoEntity edicao;

        @JsonIgnore
        @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
        @JoinColumn(name = "ID_FORMULARIO", referencedColumnName = "ID_FORMULARIO")
        private FormularioEntity formularioEntity;
}
