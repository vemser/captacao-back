package com.br.dbc.captacao.entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "INSCRICAO")
public class InscricaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSCRICAO_SEQ")
    @SequenceGenerator(name = "INSCRICAO_SEQ", sequenceName = "SEQ_INSCRICAO", allocationSize = 1)
    @Column(name = "ID_INSCRICAO")
    private Integer idInscricao;

    @Column(name = "DATA_INSCRICAO")
    private LocalDate dataInscricao;

    @Column(name = "AVALIADO")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao avaliado;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="ID_CANDIDATO", referencedColumnName = "ID_CANDIDATO")
    private CandidatoEntity candidato;

    @OneToOne(mappedBy ="inscricao" ,fetch = FetchType.LAZY)
    private AvaliacaoEntity avaliacaoEntity;

}