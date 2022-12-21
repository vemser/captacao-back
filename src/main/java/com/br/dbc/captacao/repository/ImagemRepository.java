package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.entity.ImagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImagemRepository  extends JpaRepository<ImagemEntity, Integer> {

    Optional<ImagemEntity> findByCandidato(CandidatoEntity candidatoEntity);

    Optional<ImagemEntity> findByGestorEntity(GestorEntity gestorEntity);
}
