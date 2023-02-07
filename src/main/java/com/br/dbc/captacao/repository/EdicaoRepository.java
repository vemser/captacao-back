package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.EdicaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EdicaoRepository extends JpaRepository<EdicaoEntity, Integer> {
    Optional<EdicaoEntity> findByNome(String nome);

    @Query(nativeQuery = true , value = " SELECT * " +
            " FROM EDICAO " +
            " ORDER BY EDICAO.ID_EDICAO DESC " +
            " limit 1 "
    )
    Optional<EdicaoEntity> findLastEdicao();
}
