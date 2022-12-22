package com.br.dbc.captacao.dto.gestor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class GestorSenhaDTO {

    @NotNull
    @Size(min = 6, max = 20)
    private String senha;
}
