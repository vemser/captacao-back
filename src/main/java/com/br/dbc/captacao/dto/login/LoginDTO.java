package com.br.dbc.captacao.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {
    @NotNull
    @Schema(example = "admin@dbccompany.com.br")
    private String email;

    @NotNull
    @Schema(example = "123")
    private String senha;
}
