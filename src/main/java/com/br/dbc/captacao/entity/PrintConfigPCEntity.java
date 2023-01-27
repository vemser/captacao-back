package com.br.dbc.captacao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PRINT_CONFIG_PC")
public class PrintConfigPCEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRINT_CONFIG_PC")
    @SequenceGenerator(name = "SEQ_PRINT_CONFIG_PC", sequenceName = "SEQ_PRINT_CONFIG_PC", allocationSize = 1)
    @Column(name = "ID_PRINT_CONFIG_PC")
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
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "imagemConfigPc")
    private FormularioEntity formulario;
}
