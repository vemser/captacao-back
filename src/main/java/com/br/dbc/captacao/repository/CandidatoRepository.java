package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity, Integer> {

//    Optional<CandidatoEntity> findCandidatoEntitiesByFormulario_IdFormulario(Integer idFormulario);

    Optional<CandidatoEntity> findByEmail(String email);

    CandidatoEntity findCandidatoEntitiesByEmail(String email);

    @Query(" select new com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO(" +
            " c.idCandidato," +
            " c.nome," +
            " c.email," +
            " c.notaProva," +
            " t.nome," +
            " c.edicao.nome)" +
            "  from CANDIDATO c " +
            " left join c.formularioEntity.trilhaEntitySet t" +
            " where (:nomeCompleto is null or UPPER(c.nome) LIKE  upper(concat('%',:nomeCompleto,'%')))" +
            " and (:nomeEdicao is null or c.edicao.nome = :nomeEdicao)" +
            " and (:nomeTrilha is null or t.nome = :nomeTrilha)" +
            " and(:emailCandidato is null or c.email = :emailCandidato)")
    Page<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(String nomeCompleto, String nomeTrilha, String nomeEdicao, Pageable pageable, String emailCandidato);


    List<CandidatoEntity> findCandidatoEntitiesByFormularioEntity_TrilhaEntitySet(TrilhaEntity trilhaEntity);


    List<CandidatoEntity> findCandidatoEntitiesByEdicao(EdicaoEntity edicao);

    @Query(" SELECT obj " +
            " FROM CANDIDATO obj " +
            " WHERE obj.notaProva > 1 ")
    Page<CandidatoEntity> findByNota(Pageable pageable);

    @Query("SELECT DISTINCT c FROM CANDIDATO c " +
            " INNER JOIN EDICAO e " +
            " ON e.idEdicao = c.idEdicao " +
            " INNER JOIN FORMULARIO f " +
            " ON c.formularioEntity.idFormulario =  f.idFormulario" +
            " INNER JOIN f.trilhaEntitySet ts " +
            " WHERE (:nome is null or UPPER(c.nome) LIKE UPPER(CONCAT('%', :nome, '%')))" +
            " AND (:email is null or UPPER(c.email) = UPPER(:email))" +
            " AND (:edicao is null or UPPER(e.nome) = UPPER(:edicao) AND e.idEdicao = c.idEdicao)" +
            " AND (:trilha is null or UPPER(ts.nome) = UPPER(:trilha)) " )
    Page<CandidatoEntity> filtrarCandidatos(Pageable pageable, String nome, String email, String edicao, String trilha);
}
