package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.PrintConfigPCEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrintConfigPCRepository  extends JpaRepository<PrintConfigPCEntity, Integer> {

    Optional<PrintConfigPCEntity> findByFormulario(FormularioEntity formulario);
}
