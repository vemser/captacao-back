package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.gestor.GestorEmailNomeCargoDTO;
import com.br.dbc.captacao.entity.CargoEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;

import java.util.HashSet;
import java.util.Set;

public class GestorFactory {
    public static GestorEntity getGestorEntity() {
        Set<CargoEntity> cargoEntitySet = new HashSet<>();
        cargoEntitySet.add(CargoFactory.getCargoEntity());
        GestorEntity gestorEntity = new GestorEntity();
        gestorEntity.setSenha("123");
        gestorEntity.setIdGestor(1);
        gestorEntity.setAtivo(TipoMarcacao.T);
        gestorEntity.setNome("Heloise Isabela Lopes");
        gestorEntity.setCidade("Santana");
        gestorEntity.setEstado("AP");
        gestorEntity.setEmail("julio.gabriel@dbccompany.com.br");
        gestorEntity.setCargoEntity(cargoEntitySet);
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

    public static GestorEmailNomeCargoDTO getGestorNomeEmailCargoDTO(){
        GestorEmailNomeCargoDTO gestorEmailNomeCargoDTO = new GestorEmailNomeCargoDTO();
        gestorEmailNomeCargoDTO.setNome("Kaio");
        gestorEmailNomeCargoDTO.setCargo(CargoFactory.getCargoDTO());
        gestorEmailNomeCargoDTO.setEmail("Dbc@dbccompany.com.br");
        return gestorEmailNomeCargoDTO;
    }
}
