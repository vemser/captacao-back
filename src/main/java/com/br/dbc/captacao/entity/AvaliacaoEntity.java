package com.br.dbc.captacao.entity;


import com.br.dbc.captacao.enums.TipoMarcacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity(name = "AVALIACAO")
public class AvaliacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AVALIACAO")
    @SequenceGenerator(name = "SEQ_AVALIACAO", sequenceName = "SEQ_AVALIACAO", allocationSize = 1)
    @Column(name = "ID_AVALIACAO")
    private Integer idAvaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GESTOR", referencedColumnName = "ID_GESTOR")
    private GestorEntity avaliador;

    @Column(name = "APROVADO")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao aprovado;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "ID_INSCRICAO", referencedColumnName = "ID_INSCRICAO")
    private InscricaoEntity inscricao;
}