package com.br.dbc.captacao.dto.gestor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GestorSenhaDTO {

    @NotNull
    @Size(min = 6, max = 20)
    private String senha;
}
