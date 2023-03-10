package com.br.dbc.captacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CargoDTO {

    @Schema(description = "id do Cargo.", example = "1")
    private Integer idCargo;

    @Schema(description = "Nome do cargo do gestor.", example = "ROLE_INSTRUTOR")
    private String nome;
}
