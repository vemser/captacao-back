package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.entity.PrintConfigPCEntity;

import java.util.HexFormat;

public class PrintConfigPCFactory {

    public static PrintConfigPCEntity getPrintEntity(){
        PrintConfigPCEntity printConfigPCEntity = new PrintConfigPCEntity();
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        printConfigPCEntity.setFormulario(FormularioFactory.getFormularioEntity());
        printConfigPCEntity.setTipo(".png");
        printConfigPCEntity.setNome(".png");
        printConfigPCEntity.setIdImagem(1);
        printConfigPCEntity.setData(bytes);
        return printConfigPCEntity;
    }
}
