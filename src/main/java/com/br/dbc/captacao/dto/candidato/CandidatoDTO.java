package com.br.dbc.captacao.dto.candidato;

import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.enums.TipoMarcacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoDTO {

    private Integer idCandidato;

    private String nome;

    private LocalDate dataNascimento;

    private String email;

    private String telefone;

    private String rg;

    private String cpf;

    private String estado;

    private String cidade;

    private String pcd;

    private String observacoes;

    private Double notaProva;

    private Double notaEntrevistaComportamental;

    private Double notaEntrevistaTecnica;

    private TipoMarcacao ativo;

    private String parecerComportamental;

    private String parecerTecnico;

    private Double media;

    private List<LinguagemDTO> linguagens;

    private EdicaoDTO edicao;

    private FormularioDTO formulario;

    private Integer imagem;

}
