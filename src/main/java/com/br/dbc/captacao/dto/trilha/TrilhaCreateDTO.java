package com.br.dbc.captacao.dto.trilha;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TrilhaCreateDTO {

    @NotBlank(message = "O campo nome não deve ser vazio")
    @NotNull(message = "O campo nome não deve ser nulo.")
    private String nome;
}
