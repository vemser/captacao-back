package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.entity.CargoEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CargoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final ObjectMapper objectMapper;

    public CargoEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return cargoRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Cargo não encontrado!"));
    }

    public CargoEntity findByNome(String nome) throws RegraDeNegocioException {
        nome = nome.trim().toUpperCase();
        return cargoRepository.findByNome(nome)
                .orElseThrow(() -> new RegraDeNegocioException("Cargo não encontrado!"));
    }

    public CargoDTO convertToDTO(CargoEntity cargoEntity) {
        return objectMapper.convertValue(cargoEntity, CargoDTO.class);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        cargoRepository.deleteById(id);
    }

    public CargoEntity convertToEntity(CargoDTO cargo) {
        return objectMapper.convertValue(cargo, CargoEntity.class);
    }
}
