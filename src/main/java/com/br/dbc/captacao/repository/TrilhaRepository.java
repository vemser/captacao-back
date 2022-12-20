package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.TrilhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrilhaRepository extends JpaRepository<TrilhaEntity, Integer> {
    Optional<TrilhaEntity> findByNome(String nome);

}
