package com.br.dbc.captacao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "EDICAO")
public class EdicaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EDICAO")
    @SequenceGenerator(name = "SEQ_EDICAO", sequenceName = "SEQ_EDICAO", allocationSize = 1)
    @Column(name = "ID_EDICAO")
    private Integer idEdicao;

    @Column(name = "NOME")
    private String nome;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "edicao")
    private Set<CandidatoEntity> candidatoEntities;
}
