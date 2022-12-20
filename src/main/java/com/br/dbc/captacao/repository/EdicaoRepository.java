package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.EdicaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EdicaoRepository extends JpaRepository<EdicaoEntity, Integer> {
    Optional<EdicaoEntity> findByNome(String nome);
}
