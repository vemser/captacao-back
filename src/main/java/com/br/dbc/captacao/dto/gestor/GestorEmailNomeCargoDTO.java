package com.br.dbc.captacao.dto.gestor;

import com.br.dbc.captacao.dto.CargoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GestorEmailNomeCargoDTO {

    @Schema(description = "(opcional) nome do gestor", example = "")
    private String nome;
    @Schema(description = "(opcional) email do gestor", example = "")
    private String email;
    @Schema(description = "cargo do gestor", example = "ADMINISTRADOR")
    private CargoDTO cargo;
}
