package com.br.dbc.captacao.repository;


import com.br.dbc.captacao.entity.VwCandidatosUltimaEdicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VwCandidatosUltimaEdicaoRepository extends JpaRepository<VwCandidatosUltimaEdicao, Integer> {
}
