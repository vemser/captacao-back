package com.br.dbc.captacao.entity;

import com.br.dbc.captacao.enums.Legenda;
import com.br.dbc.captacao.enums.Parecer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "ENTREVISTAS")
public class EntrevistaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENTREVISTA")
    @SequenceGenerator(name = "SEQ_ENTREVISTA", sequenceName = "SEQ_ID_ENTREVISTAS", allocationSize = 1)
    @Column(name = "id_entrevista")
    private Integer idEntrevista;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_candidato", referencedColumnName = "id_candidato")
    private CandidatoEntity candidatoEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_gestor")
    private GestorEntity gestorEntity;

    @Column(name = "data_hora_entrevista")
    private LocalDateTime dataEntrevista;

    @Column(name = "observacoes")
    private String observacoes;

//    Essas colunas devem ser apagadas? Elas eram do Ftf e nao foram incluídas no novo script.
//    @Column(name = "cidade")
//    private String cidade;
//
//    @Column(name = "estado")
//    private String estado;

    @Column(name = "legenda")
    @Enumerated(EnumType.STRING)
    private Legenda legenda;

    @Column(name = "parecer_comp")
    @Enumerated(EnumType.STRING)
    private Parecer parecerComportamental;

    @Column(name = "parecer_tecnico")
    @Enumerated(EnumType.STRING)
    private Parecer parecerTecnico;
}
