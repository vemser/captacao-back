package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.AvaliacaoEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Integer> {

    AvaliacaoEntity findAvaliacaoEntitiesByInscricao_IdInscricao(Integer idInscricao);

    Page<AvaliacaoEntity> findByAprovado(Pageable pageable, TipoMarcacao tipo);

    @Query("SELECT DISTINCT a FROM AVALIACAO a " +
            " INNER JOIN CANDIDATO c " +
            " ON (a.inscricao.candidato.idCandidato = c.idCandidato) " +
            " INNER JOIN EDICAO e " +
            " ON e.idEdicao = a.inscricao.candidato.edicao.idEdicao " +
            " INNER JOIN FORMULARIO f " +
            " ON c.formularioEntity.idFormulario = f.idFormulario " +
            " INNER JOIN f.trilhaEntitySet ts" +
            " WHERE (:email is null or UPPER(c.email) = UPPER(:email) AND a.inscricao.candidato.idCandidato = c.idCandidato) " +
            " AND (:edicao is null or UPPER(e.nome) = UPPER(:edicao) AND e.idEdicao = c.edicao.idEdicao) " +
            " AND (:trilha is null or UPPER(ts.nome) = UPPER(:trilha)) " +
            " AND (a.aprovado = 'T') ")
    Page<AvaliacaoEntity> filtrarAvaliacoes(Pageable pageable, String email, String edicao, String trilha);
}