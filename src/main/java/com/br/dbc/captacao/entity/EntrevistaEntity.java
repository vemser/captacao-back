package com.br.dbc.captacao.entity;

import com.br.dbc.captacao.enums.Legenda;
import com.br.dbc.captacao.enums.Parecer;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    @JoinColumn(name = "id_gestor", referencedColumnName = "id_gestor")
    private GestorEntity gestorEntity;

    @Column(name = "data_hora")
    private LocalDateTime dataEntrevista;

    @Column(name = "observacoes")
    private String observacoes;


    @Column(name = "legenda")
    @Enumerated(EnumType.STRING)
    private Legenda legenda;

    @Column(name="avaliadoentrevista")
    @Enumerated(EnumType.ORDINAL)
    private TipoMarcacao avaliado;
}
