package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.trilha.TrilhaCreateDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.TrilhaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrilhaService {

    private final ObjectMapper objectMapper;
    private final TrilhaRepository trilhaRepository;

    public TrilhaDTO create(TrilhaCreateDTO trilhaCreateDTO){
        TrilhaEntity trilha = objectMapper.convertValue(trilhaCreateDTO, TrilhaEntity.class);
        trilhaRepository.save(trilha);
        return convertToDTO(trilha);
    }

    public List<TrilhaDTO> list() {
        return trilhaRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TrilhaEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return trilhaRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Trilha não encontrada!"));
    }

    public TrilhaEntity findByNome(String nome) throws RegraDeNegocioException {
        nome = nome.trim().toUpperCase();
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
        findById(id);
        trilhaRepository.deleteById(id);
    }

    public Set<TrilhaEntity> convertToEntity(Set<TrilhaDTO> trilhas) {
        return trilhas.stream()
                .map(trilhaDtos -> objectMapper.convertValue(trilhaDtos, TrilhaEntity.class))
                .collect(Collectors.toSet());
    }

    private TrilhaDTO convertToDTO(TrilhaEntity trilhaEntity) {
        return objectMapper.convertValue(trilhaEntity, TrilhaDTO.class);
    }
}
