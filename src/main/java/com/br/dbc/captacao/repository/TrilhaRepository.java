package com.br.dbc.captacao.repository;

import com.br.dbc.captacao.entity.TrilhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrilhaRepository extends JpaRepository<TrilhaEntity, Integer> {
    Optional<TrilhaEntity> findByNome(String nome);

    @Query(value = " SELECT COUNT (ID_TRILHA) " +
                   " FROM CAPTACAO.TRILHA_FORM tf " +
                   " WHERE tf.ID_TRILHA = :id", nativeQuery=true)
    Integer verifyRelacaoTrilha(Integer id);
}
