package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.TrilhaEntity;
public class TrilhaFactory {
    public static TrilhaEntity getTrilhaEntity() {
        TrilhaEntity trilha = new TrilhaEntity();
        trilha.setIdTrilha(1);
        trilha.setNome("BACKEND");

        return trilha;
    }

    public static TrilhaDTO getTrilhaDTO() {
        return new TrilhaDTO(1);
    }
}
