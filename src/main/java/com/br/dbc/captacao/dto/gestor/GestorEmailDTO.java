package com.br.dbc.captacao.dto.gestor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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
