package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.AvaliacaoEntity;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Integer> {

    AvaliacaoEntity findAvaliacaoEntitiesByInscricao_IdInscricao(Integer idInscricao);

    List<AvaliacaoEntity> findAvaliacaoEntitiesByInscricao_Candidato_Email(String email);

    @Query("SELECT DISTINCT a FROM AVALIACAO a " +
            " inner join CANDIDATO c " +
            " on (a.inscricao.candidato.idCandidato = c.idCandidato) " +
            " inner join EDICAO e " +
            " on e.idEdicao = a.inscricao.candidato.edicao.idEdicao " +
            " inner join FORMULARIO f " +
            " on c.formularioEntity.idFormulario = f.idFormulario " +
            " inner join f.trilhaEntitySet ts" +
            " WHERE (:email is null or UPPER(c.email) = UPPER(:email) AND a.inscricao.candidato.idCandidato = c.idCandidato) " +
            " and (:edicao is null or UPPER(e.nome) = UPPER(:edicao) AND e.idEdicao = c.edicao.idEdicao) " +
            " and (:trilha is null or UPPER(ts.nome) = upper(:trilha)) ")
    Page<AvaliacaoEntity> filtrarAvaliacoes(Pageable pageable, String email, String edicao, String trilha);

    List<AvaliacaoEntity> findAvaliacaoEntitiesByInscricao_Candidato_Edicao(EdicaoEntity edicao);
}