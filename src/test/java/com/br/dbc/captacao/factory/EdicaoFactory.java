package com.br.dbc.captacao.factory;


import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.entity.EdicaoEntity;

public class EdicaoFactory {

    public static EdicaoEntity getEdicaoEntity() {
        EdicaoEntity edicao = new EdicaoEntity();
        edicao.setIdEdicao(1);
        edicao.setNome("Edição 10");

        return edicao;
    }

    public static EdicaoDTO getEdicaoDTO() {
        return new EdicaoDTO("Edição 10");
    }
}
