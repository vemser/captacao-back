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
@Entity(name = "CURRICULO")
public class CurriculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CURRICULO")
    @SequenceGenerator(name = "SEQ_CURRICULO", sequenceName = "SEQ_CURRICULO", allocationSize = 1)
    @Column(name = "ID_CURRICULO")
    private Integer idCurriculo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "dado")
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] data;

    @JsonIgnore
    @OneToOne(mappedBy = "curriculoEntity", fetch = FetchType.LAZY)
    private FormularioEntity formularioEntity;
}
