package com.br.dbc.captacao.dto.gestor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GestorEmailDTO {

    @NotNull
    @Email
    private String email;

    private String url;
}
