package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.CargoEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GestorRepository extends JpaRepository<GestorEntity, Integer> {

    List<GestorEntity> findGestorEntitiesByCargoEntityAndNomeIgnoreCaseOrCargoEntityAndEmailIgnoreCase(CargoEntity cargo, String nome, CargoEntity cargoEntity, String email);

    List<GestorEntity> findByAtivo(TipoMarcacao ativo);

    GestorEntity findByEmail(String email);

    Optional<GestorEntity> findGestorEntityByEmailEqualsIgnoreCase(String email);

}
