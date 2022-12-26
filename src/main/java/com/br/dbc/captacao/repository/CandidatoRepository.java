package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
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

    List<CandidatoEntity> findCandidatoEntitiesByEmail(String email);

//    @Query(" select new com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO(" +
//            " c.idCandidato," +
//            " c.nome," +
//            " c.email," +
//            " c.notaProva," +
//            " t.nome," +
//            " c.edicao.nome)" +
//            "  from CANDIDATO c " +
//            " left join c.trilha t" +
//            " where (:nomeCompleto is null or UPPER(c.nome) LIKE  upper(concat('%',:nomeCompleto,'%')))" +
//            " and (:nomeEdicao is null or c.edicao.nome = :nomeEdicao)" +
//            " and (:nomeTrilha is null or c.trilha.nome = :nomeTrilha)")
//    Page<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(String nomeCompleto, String nomeTrilha, String nomeEdicao, Pageable pageable);
//

    List<CandidatoEntity> findCandidatoEntitiesByFormularioEntity_TrilhaEntitySet(TrilhaEntity trilhaEntity);


    List<CandidatoEntity> findCandidatoEntitiesByEdicao(EdicaoEntity edicao);

}
