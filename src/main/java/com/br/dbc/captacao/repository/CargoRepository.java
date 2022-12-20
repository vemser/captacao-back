package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<CargoEntity, Integer> {
    Optional<CargoEntity> findByNome(String nome);
}
