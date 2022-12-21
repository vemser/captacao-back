package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.FormularioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FormularioRepository extends JpaRepository<FormularioEntity, Integer> {
}
