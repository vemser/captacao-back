package com.br.dbc.captacao.dto.inscricao;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InscricaoCreateDTO {

    @NotNull
    private Integer idCandidato;

}