package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.entity.CurriculoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;

import java.util.HexFormat;

import static com.br.dbc.captacao.factory.FormularioFactory.getFormularioEntity;

public class CurriculoFactory {
    public static CurriculoEntity getCurriculoEntity() {
        FormularioEntity formularioEntity = getFormularioEntity();
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        return new CurriculoEntity(2,
                "curriculo",
                "pdf",
                bytes,
                formularioEntity);
    }
}
