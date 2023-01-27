package com.br.dbc.captacao.dto.gestor;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GestorSenhaDTO {

    @NotNull
    @Size(min = 6, max = 20)
    private String senha;
}
