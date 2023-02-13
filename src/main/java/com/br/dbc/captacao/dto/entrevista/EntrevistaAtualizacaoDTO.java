package com.br.dbc.captacao.dto.entrevista;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EntrevistaAtualizacaoDTO {
    @NotNull
    @FutureOrPresent(message = "Entrevista precisa ser no presente ou futuro.")
    @Schema(description = "Dia/Mês/Ano que irá ocorrer a entrevista.")
    private LocalDateTime dataEntrevista;
    @NotNull
    @Schema(description = "Cidade em que o usuário irá realizar a entrevista")
    private String cidade;
    @NotNull
    @Schema(description = "Estado em que o usuário irá realizar a entrevista")
    private String estado;
    @NotNull
    @Schema(description = "Observações referentes a entrevista.")
    private String observacoes;
    @NotNull
    @Schema(description = "Avaliado ou não", example = "T")
    private String avaliado;
}
