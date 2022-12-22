package com.br.dbc.captacao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TRILHA")
public class TrilhaEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRILHA")
    @SequenceGenerator(name = "SEQ_TRILHA", sequenceName = "SEQ_TRILHA", allocationSize = 1)
    @Column(name = "id_trilha")
    private Integer idTrilha;

    @Column(name = "descricao")
    private String nome;

    @JsonIgnore
    @ManyToMany(mappedBy = "trilhaEntitySet", fetch = FetchType.LAZY)
    private Set<FormularioEntity> formularios;
}
