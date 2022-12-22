package com.br.dbc.captacao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "IMAGEM")
public class ImagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FOTOS")
    @SequenceGenerator(name = "SEQ_FOTOS", sequenceName = "SEQ_FOTOS", allocationSize = 1)
    @Column(name = "ID_IMAGEM")
    private Integer idImagem;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "dado")
    @Lob
    private byte[] data;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CANDIDATO", referencedColumnName = "ID_CANDIDATO")
    private CandidatoEntity candidato;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GESTOR", referencedColumnName = "ID_GESTOR")
    private GestorEntity gestorEntity;
}
