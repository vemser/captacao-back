package com.br.dbc.captacao.dto.entrevista;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrevistaCreateDTO {
    @NotNull
    @Schema(description = "Candidato que irá participar da entrevista.")
    private String candidatoEmail;

    @NotNull
    @Schema(description = "Dia/Mês/Ano que irá ocorrer a entrevista.")
    private LocalDateTime dataEntrevista;

    @NotNull
    @Schema(description = "Observações referentes a entrevista.")
    private String observacoes;

    @NotNull
    @Schema(description = "Avaliado ou não", example = "T")
    private String avaliado;
}
