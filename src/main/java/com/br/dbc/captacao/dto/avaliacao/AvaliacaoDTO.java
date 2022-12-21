package com.br.dbc.captacao.dto.avaliacao;

import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.enums.TipoMarcacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoDTO {

    private Integer idAvaliacao;

    private GestorDTO avaliador;

    private TipoMarcacao aprovado;

    private InscricaoDTO inscricao;
}