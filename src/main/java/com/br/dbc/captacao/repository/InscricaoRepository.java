package com.br.dbc.captacao.repository;


import com.br.dbc.captacao.entity.InscricaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<InscricaoEntity, Integer> {

    Optional<InscricaoEntity> findInscricaoEntitiesByCandidato_IdCandidato(Integer idCandidato);

    @Query("SELECT DISTINCT i FROM INSCRICAO i " +
            " INNER JOIN CANDIDATO c " +
            " ON (i.idCandidato = c.idCandidato)" +
            " INNER JOIN EDICAO e " +
            " ON e.idEdicao = c.idEdicao " +
            " INNER JOIN FORMULARIO f " +
            " ON c.formularioEntity.idFormulario =  f.idFormulario" +
            " INNER JOIN f.trilhaEntitySet ts " +
            " WHERE (:email is null or UPPER(c.email) = UPPER(:email) AND i.idCandidato = c.idCandidato)" +
            " AND (:edicao is null or UPPER(e.nome) = UPPER(:edicao) AND e.idEdicao = c.idEdicao)" +
            " AND (:trilha is null or UPPER(ts.nome) = UPPER(:trilha)) " )
    Page<InscricaoEntity> filtrarInscricoes(Pageable pageable, String email, String edicao, String trilha);
}
