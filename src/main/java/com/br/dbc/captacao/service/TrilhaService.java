package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.trilha.TrilhaCreateDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.TrilhaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrilhaService {

    private final ObjectMapper objectMapper;
    private final TrilhaRepository trilhaRepository;

    public TrilhaDTO create(TrilhaCreateDTO trilhaCreateDTO) throws RegraDeNegocioException {
        Optional<TrilhaEntity> trilha = trilhaRepository.findByNome(trilhaCreateDTO.getNome().trim());
        if (trilha.isPresent()) {
            throw new RegraDeNegocioException("Trilha já existe!");
        }

        TrilhaEntity trilhaEntity = objectMapper.convertValue(trilhaCreateDTO, TrilhaEntity.class);
        trilhaEntity.setNome(trilhaEntity.getNome().trim().toUpperCase());
        TrilhaEntity trilhaSalva = trilhaRepository.save(trilhaEntity);
        return objectMapper.convertValue(trilhaSalva, TrilhaDTO.class);
    }

    public List<TrilhaDTO> list() {
        return trilhaRepository.findAll().stream()
                .map(trilha -> objectMapper.convertValue(trilha, TrilhaDTO.class)).toList();
    }

    public TrilhaEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return trilhaRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Trilha não encontrada!"));
    }

    public TrilhaEntity findByNome(String nome) throws RegraDeNegocioException {
        nome = nome.trim();
        return trilhaRepository.findByNome(nome)
                .orElseThrow(() -> new RegraDeNegocioException("Trilha não encontrada!"));
    }

    public Set<TrilhaEntity> findListaTrilhas(List<Integer> idTrilhas) throws RegraDeNegocioException {
        List<TrilhaEntity> trilhaEntities = new ArrayList<>();
        for (Integer id : idTrilhas) {
            trilhaEntities.add(findById(id));
        }
        return new HashSet<>(trilhaEntities);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        TrilhaEntity trilha = trilhaRepository.findById(id).orElseThrow(() -> new RegraDeNegocioException("Trilha não encontrada!"));
        Integer countRelacaoTrilha = trilhaRepository.verifyRelacaoTrilha(id);
        if (countRelacaoTrilha > 0) {
            throw new RegraDeNegocioException("Trilha não pode ser excluída porque possuí inscrições atribuídas a ela!");
        }
        trilhaRepository.delete(trilha);
    }

    public Set<TrilhaEntity> convertToEntity(Set<TrilhaDTO> trilhas) {
        return trilhas.stream()
                .map(trilhaDtos -> objectMapper.convertValue(trilhaDtos, TrilhaEntity.class))
                .collect(Collectors.toSet());
    }

    public Set<TrilhaDTO> convertToDTO(Set<TrilhaEntity> trilhas) {
        return trilhas.stream()
                .map(trilha -> objectMapper.convertValue(trilha, TrilhaDTO.class)).collect(Collectors.toSet());
    }
}
