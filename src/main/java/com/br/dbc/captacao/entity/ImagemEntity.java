package com.br.dbc.captacao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "IMAGEM")
public class ImagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_IMAGEM")
    @SequenceGenerator(name = "SEQ_IMAGEM", sequenceName = "SEQ_IMAGEM", allocationSize = 1)
    @Column(name = "ID_IMAGEM")
    private Integer idImagem;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "dado")
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
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
