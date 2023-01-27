package com.br.dbc.captacao.dto.gestor;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class GestorEmailDTO {

    @NotNull
    @Email
    private String email;

    private String url;
}
