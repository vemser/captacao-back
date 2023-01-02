package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;

public class GestorFactory {
    public static GestorEntity getGestorEntity() {
        GestorEntity gestorEntity = new GestorEntity();
        gestorEntity.setSenha("123");
        gestorEntity.setIdGestor(1);
        gestorEntity.setAtivo(TipoMarcacao.T);
        gestorEntity.setNome("Heloise Isabela Lopes");
        gestorEntity.setCidade("Santana");
        gestorEntity.setEstado("AP");
        gestorEntity.setEmail("julio.gabriel@dbccompany.com.br");
        return gestorEntity;
    }

    public static GestorDTO getGestorDTO() {
        GestorDTO usuarioDTO = new GestorDTO();
        usuarioDTO.setIdGestor(1);
        usuarioDTO.setNome("Débora Sophia da Silva");
        usuarioDTO.setEmail("julio.gabriel@dbccompany.com.br");
        usuarioDTO.setAtivo(TipoMarcacao.T);

        return usuarioDTO;
    }
}
