package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.entity.ImagemEntity;

import java.util.HexFormat;

import static com.br.dbc.captacao.factory.CandidatoFactory.*;

public class ImageFactory {
    public static ImagemEntity getImageEntity() {
        ImagemEntity imageEntity = new ImagemEntity();
        imageEntity.setCandidato(getCandidatoEntity());
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        imageEntity.setData(bytes);
        imageEntity.setIdImagem(1);
        imageEntity.setTipo(".png");
        imageEntity.setNome(".png");
        return imageEntity;
    }

    public static ImagemEntity getImageUsuario() {
        ImagemEntity imageEntity = new ImagemEntity();
        imageEntity.setGestorEntity(GestorFactory.getGestorEntity());
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        imageEntity.setData(bytes);
        imageEntity.setIdImagem(1);
        imageEntity.setTipo(".png");
        imageEntity.setNome(".png");
        return imageEntity;
    }
}