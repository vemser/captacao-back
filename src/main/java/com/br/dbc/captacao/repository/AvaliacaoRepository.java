package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.AvaliacaoEntity;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Integer> {

    AvaliacaoEntity findAvaliacaoEntitiesByInscricao_IdInscricao(Integer idInscricao);


    List<AvaliacaoEntity> findAvaliacaoEntitiesByInscricao_Candidato_Email(String email);

    List<AvaliacaoEntity> findAvaliacaoEntitiesByInscricao_Candidato_FormularioEntity_TrilhaEntitySet(TrilhaEntity trilhaEntity);

    List<AvaliacaoEntity> findAvaliacaoEntitiesByInscricao_Candidato_Edicao(EdicaoEntity edicao);
}