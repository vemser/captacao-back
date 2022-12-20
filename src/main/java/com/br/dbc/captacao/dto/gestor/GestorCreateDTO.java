package com.br.dbc.captacao.dto.gestor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GestorCreateDTO {

    @NotNull
    @Size(min = 3, max = 255, message = "O nome deve ter de 3 a 255 caracteres")
    @Schema(description = "Nome do gestor", example = "Márcia da Silva Santos")
    private String nome;

    @Email
    @NotNull
    private String email;

    private String senha;

    private Integer tipoCargo;
}
