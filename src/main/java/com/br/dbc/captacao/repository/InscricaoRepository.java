package com.br.dbc.captacao.repository;


import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.entity.InscricaoEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<InscricaoEntity, Integer> {

    Optional<InscricaoEntity> findInscricaoEntitiesByCandidato_IdCandidato(Integer idCandidato);

    @Query("SELECT i FROM INSCRICAO i " +
            " INNER JOIN CANDIDATO c " +
            " ON UPPER(c.email) = UPPER(:email) AND " +
            " i.idCandidato = c.idCandidato ")
    InscricaoEntity findInscricaoByEmail(String email);

    List<InscricaoEntity> findInscricaoEntitiesByCandidato_FormularioEntity_TrilhaEntitySet(TrilhaEntity trilhaEntity);

    List<InscricaoEntity> findInscricaoEntitiesByCandidato_Edicao(EdicaoEntity edicao);

    @Query("select i from INSCRICAO i " +
            "WHERE i.avaliacaoEntity.aprovado='T'")
    List<InscricaoEntity> listarInscricoesAprovadas();
}
