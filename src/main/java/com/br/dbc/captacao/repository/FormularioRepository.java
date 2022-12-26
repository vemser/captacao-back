package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormularioRepository extends JpaRepository<FormularioEntity, Integer> {
    Optional<FormularioEntity> findByCandidatoEmail(String candidatoEmail);

    @Query("select f from FORMULARIO f " +
            "WHERE (f.curriculoEntity is not null and f.idFormulario is not null)")
    Page<FormularioEntity> listarFormulariosSemVazios(Pageable pageable);
}
