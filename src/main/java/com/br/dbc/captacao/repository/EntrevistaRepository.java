package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EntrevistaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntrevistaRepository extends JpaRepository<EntrevistaEntity, Integer> {

    Optional<EntrevistaEntity> findByCandidatoEntity(CandidatoEntity candidatoEntity);

    @Query("SELECT obj " +
            "FROM ENTREVISTAS obj " +
            "WHERE obj.dataEntrevista BETWEEN :inicio AND :fim")
    List<EntrevistaEntity> findByDataEntrevistaBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT e FROM ENTREVISTAS e " +
            "WHERE EXTRACT (MONTH FROM e.dataEntrevista) = :mes AND EXTRACT (YEAR FROM e.dataEntrevista) = :ano" +
            " ORDER BY e.dataEntrevista ASC  ")
    Page<EntrevistaEntity> findAllByMes(Integer mes, Integer ano, PageRequest pageRequest);

    @Query("SELECT obj " +
            "FROM ENTREVISTAS obj " +
            "inner join obj.candidatoEntity ca "  +
            "inner join ca.formularioEntity fo " +
            "inner join fo.trilhaEntitySet ti " +
            "WHERE UPPER(ti.nome) LIKE UPPER(:trilha)")
    List<EntrevistaEntity> findAllByTrilha(String trilha);

    @Query(" SELECT e FROM ENTREVISTAS e " +
            " INNER JOIN e.candidatoEntity c " +
            " WHERE e.legenda = 'PENDENTE' AND c.notaProva >= 60 ")
    List<EntrevistaEntity> findEntrevista();
}
