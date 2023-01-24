package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.CandidatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity, Integer> {

    Optional<CandidatoEntity> findCandidatoEntitiesByEmailAndEdicao_Nome(String email, String edicao);
    Optional<CandidatoEntity> findByEmail(String email);

    @Query(" SELECT obj " +
            " FROM CANDIDATO obj " +
            " WHERE obj.notaProva >= 60 ")
    Page<CandidatoEntity> findByNota(Pageable pageable);

    @Query(" SELECT obj " +
            " FROM CANDIDATO obj " +
            " WHERE obj.media >= 60 ")
    List<CandidatoEntity> findByMedia();

    @Query("SELECT DISTINCT c FROM CANDIDATO c " +
            " INNER JOIN EDICAO e " +
            " ON e.idEdicao = c.idEdicao " +
            " INNER JOIN FORMULARIO f " +
            " ON c.formularioEntity.idFormulario =  f.idFormulario" +
            " INNER JOIN f.trilhaEntitySet ts " +
            " WHERE (:email is null or UPPER(c.email) = UPPER(:email))" +
            " AND (:edicao is null or UPPER(e.nome) = UPPER(:edicao) AND e.idEdicao = c.idEdicao)" +
            " AND (:trilha is null or UPPER(ts.nome) = UPPER(:trilha)) " +
            " AND (c.notaProva >= 60) " )
    Page<CandidatoEntity> filtrarCandidatosAptosEntrevista(Pageable pageable, String email, String edicao, String trilha);

    @Query("SELECT DISTINCT c FROM CANDIDATO c " +
            " INNER JOIN EDICAO e " +
            " ON e.idEdicao = c.idEdicao " +
            " INNER JOIN FORMULARIO f " +
            " ON c.formularioEntity.idFormulario =  f.idFormulario" +
            " INNER JOIN f.trilhaEntitySet ts " +
            " WHERE (:email is null or UPPER(c.email) = UPPER(:email))" +
            " AND (:edicao is null or UPPER(e.nome) = UPPER(:edicao) AND e.idEdicao = c.idEdicao)" +
            " AND (:trilha is null or UPPER(ts.nome) = UPPER(:trilha)) " +
            " AND (c.media >= 60) " )
    Page<CandidatoEntity> filtrarCandidatosAprovados(Pageable pageable, String email, String edicao, String trilha);
}
