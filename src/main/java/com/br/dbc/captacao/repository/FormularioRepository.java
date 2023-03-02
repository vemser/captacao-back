package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorEdicaoDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorGeneroDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorNeurodiversidadeDTO;
import com.br.dbc.captacao.entity.FormularioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormularioRepository extends JpaRepository<FormularioEntity, Integer> {
    Optional<FormularioEntity> findByCandidatoEmail(String candidatoEmail);

    @Query("select f from FORMULARIO f " +
            "WHERE (f.curriculoEntity is not null and f.idFormulario is not null)")
    Page<FormularioEntity> listarFormulariosSemVazios(Pageable pageable);


    @Query("  select new com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorGeneroDTO(f.genero, count(c))" +
            "   from FORMULARIO f" +
            "   join f.candidato c" +
            "   where c.idEdicao = (select max(e.idEdicao) from EDICAO e) " + // recupera sempre os dados da ultima edição
            "   group by f.genero ")
    List<RelatorioQuantidadePessoasInscritasPorGeneroDTO> recuperarQuantidadeDePessoasInscritasPorGenero();

    @Query("  select new com.br.dbc.captacao.dto.relatorios.RelatorioQuantidadePessoasInscritasPorNeurodiversidadeDTO(f.neurodiversidade, count(c))" +
            "   from FORMULARIO f" +
            "   join f.candidato c" +
            "   where c.idEdicao = (select max(e.idEdicao) from EDICAO e) " + // recupera sempre os dados da ultima edição
            "   group by f.neurodiversidade ")
    List<RelatorioQuantidadePessoasInscritasPorNeurodiversidadeDTO> recuperarQuantidadeDePessoasInscritasPorNeurodiversidade();


}
