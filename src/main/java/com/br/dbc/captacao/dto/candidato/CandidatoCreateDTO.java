package com.br.dbc.captacao.dto.candidato;

import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoCreateDTO {

    private String nome;

    private LocalDate dataNascimento;

    private String email;

    private String telefone;

    private String rg;

    private String cpf;

    private String estado;

    private String cidade;

    private boolean isPcdboolean;

    private TipoMarcacao ativo;

    @JsonIgnore
    private Double media;

    private Set<LinguagemDTO> linguagens;

    @Schema(example = "1ª Edição")
    private EdicaoDTO edicao;

    private Integer formulario;


}
