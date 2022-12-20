package com.br.dbc.captacao.dto.gestor;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.enums.TipoMarcacao;
import lombok.Data;

@Data
public class GestorDTO {

    private Integer idGestor;

    private String nome;

    private String email;

    private CargoDTO cargoDto;

    private TipoMarcacao ativo;
}
