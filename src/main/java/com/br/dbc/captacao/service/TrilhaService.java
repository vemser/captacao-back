package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.TrilhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrilhaService {

    private final TrilhaRepository trilhaRepository;

    public TrilhaEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return trilhaRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Trilha não encontrada!"));
    }

    public TrilhaEntity findByNome(String nome) throws RegraDeNegocioException {
        nome = nome.trim().toUpperCase();
        return trilhaRepository.findByNome(nome)
                .orElseThrow(() -> new RegraDeNegocioException("Trilha não encontrada!"));
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        trilhaRepository.deleteById(id);
    }
}
