package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorEdicaoDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorEstadoDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorPCDDTO;
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
    Optional<CandidatoEntity> findByCpf(String cpf);

    @Query(" SELECT obj " +
            " FROM CANDIDATO obj " +
            " INNER JOIN obj.edicao e " +
            " WHERE obj.notaProva >= 60 " +
            "AND :edicao is null or UPPER(e.nome) = UPPER(:edicao)")
    Page<CandidatoEntity> findByNota(Pageable pageable, String edicao);

    @Query(" SELECT obj " +
            " FROM CANDIDATO obj " +
            " INNER JOIN obj.edicao e " +
            " WHERE obj.media >= 60 " +
            "AND :edicao is null or UPPER(e.nome) = UPPER(:edicao)")
    List<CandidatoEntity> findByMedia(String edicao);

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

    @Query("  select new com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorEdicaoDTO(e.nome, count(c))" +
            "   from CANDIDATO c " +
            "   join c.edicao e " +
            "   group by e.idEdicao ")
    List<RelatorioQuantidadePessoasInscritasPorEdicaoDTO> recuperarQuantidadeDePessoasInscritasPorEdicao();

    @Query("  select new com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorEstadoDTO(c.estado, count(c))" +
            "   from CANDIDATO c " +
            "   where c.idEdicao = (select max(e.idEdicao) from EDICAO e) " + // recupera sempre os dados da ultima edição
            "   group by c.estado ")
    List<RelatorioQuantidadePessoasInscritasPorEstadoDTO> recuperarQuantidadeDePessoasInscritasPorEstado();

    @Query("  select new com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorPCDDTO(c.pcd, count(c))" +
            "   from CANDIDATO c " +
            "   where c.idEdicao = (select max(e.idEdicao) from EDICAO e) " + // recupera sempre os dados da ultima edição
            "   group by c.pcd ")
    List<RelatorioQuantidadePessoasInscritasPorPCDDTO> recuperarQuantidadeDePessoasInscritasPorPCD();


    @Query("   SELECT DISTINCT candidato," +
            "                  candidato.linguagens, " +
            "                  inscricao, " +
            "                  form, " +
            "                  form.trilhaEntitySet " +
            "             FROM CANDIDATO candidato " +
            "             JOIN INSCRICAO inscricao on (candidato.idCandidato = inscricao.idCandidato)" +
            "             JOIN candidato.edicao edicao " +
            "             JOIN candidato.formularioEntity form " +
            " where candidato.idEdicao = (select max(e.idEdicao) from EDICAO e) ")
    List<Object> filtrarCandidatosEdicaoAtualExcel();

}
