package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.entity.CargoEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CargoRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.br.dbc.captacao.factory.CargoFactory.getCargoEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CargoServiceTest {
    @InjectMocks
    private CargoService cargoService;

    @Mock
    private CargoRepository cargoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(cargoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        Integer id = 10;

        when(cargoRepository.findById(anyInt())).thenReturn(Optional.of(getCargoEntity()));
        cargoService.deleteFisico(id);

        verify(cargoRepository, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarDeleteComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(cargoRepository.findById(anyInt())).thenReturn(Optional.empty());
        cargoService.deleteFisico(id);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        Integer id = 2;

        when(cargoRepository.findById(anyInt())).thenReturn(Optional.of(getCargoEntity()));
        CargoEntity perfil = cargoService.findById(id);

        assertNotNull(perfil);
        assertEquals(perfil.getIdCargo(), id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByIdComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(cargoRepository.findById(anyInt())).thenReturn(Optional.empty());
        cargoService.findById(id);
    }

    @Test
    public void deveTestarFindByNomeComSucesso() throws RegraDeNegocioException {
        String nome = "ADMIN";

        when(cargoRepository.findByNome(anyString())).thenReturn(Optional.of(getCargoEntity()));
        CargoEntity perfil = cargoService.findByNome(nome);

        assertNotNull(perfil);
        assertEquals(perfil.getNome(), nome);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByNomeComErro() throws RegraDeNegocioException {
        String nome = "ADMIN";
        when(cargoRepository.findByNome(anyString())).thenReturn(Optional.empty());
        cargoService.findByNome(nome);
    }

    @Test
    public void deveConverterEmDTOComSucesso() {
        CargoEntity perfilEntity = getCargoEntity();
        CargoDTO perfilDTO = cargoService.convertToDTO(perfilEntity);

        assertNotNull(perfilDTO);
        assertEquals(perfilEntity.getNome(), perfilDTO.getNome());
    }
}

