package com.br.dbc.captacao.repository;


import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.InscricaoEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<InscricaoEntity, Integer> {

    Optional<InscricaoEntity> findInscricaoEntitiesByCandidato_IdCandidato(Integer idCandidato);

    List<InscricaoEntity> findInscricaoEntitiesByCandidato_Email(String email);

    List<InscricaoEntity> findInscricaoEntitiesByCandidato_FormularioEntity_TrilhaEntitySet(TrilhaEntity trilhaEntity);

    List<InscricaoEntity> findInscricaoEntitiesByCandidato_Edicao(EdicaoEntity edicao);

    @Query("select i from INSCRICAO i " +
            "WHERE i.avaliacaoEntity.aprovado='T'")
    List<InscricaoEntity> listarInscricoesAprovadas();
}
