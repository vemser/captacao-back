package com.br.dbc.captacao.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class LoginDTO {
    @NotNull
    @Schema(example = "admin@dbccompany.com.br")
    private String email;

    @NotNull
    @Schema(example = "123")
    private String senha;
}
