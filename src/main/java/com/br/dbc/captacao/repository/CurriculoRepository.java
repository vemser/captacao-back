package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.CurriculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurriculoRepository extends JpaRepository<CurriculoEntity, Integer> {
}
