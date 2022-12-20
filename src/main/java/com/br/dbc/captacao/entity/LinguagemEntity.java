package com.br.dbc.captacao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "LINGUAGEM")
public class LinguagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LINGUAGEM_SEQUENCIA")
    @SequenceGenerator(name = "LINGUAGEM_SEQUENCIA", sequenceName = "SEQ_ID_LINGUAGEM", allocationSize = 1)
    @Column(name = "ID_LINGUAGEM")
    private Integer idLinguagem;

    @Column(name = "NOME")
    private String nome;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CANDIDATO_LINGUAGEM",
            joinColumns = @JoinColumn(name = "ID_LINGUAGEM"),
            inverseJoinColumns = @JoinColumn(name = "ID_CANDIDATO")
    )
    private Set<CandidatoEntity> candidatoEntities;
}
