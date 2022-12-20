package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Integer> {

    Optional<AvaliacaoEntity> findAvaliacaoEntitiesByInscricao_IdInscricao(Integer idInscricao);

    List<AvaliacaoEntity> findAvaliacaoEntitiesByInscricao_Candidato_Email(String email);
}