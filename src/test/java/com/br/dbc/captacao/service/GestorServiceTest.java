package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.gestor.GestorEmailNomeCargoDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CargoEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.entity.LinguagemEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.CargoFactory;
import com.br.dbc.captacao.factory.GestorFactory;
import com.br.dbc.captacao.repository.GestorRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.br.dbc.captacao.factory.GestorFactory.getGestorDTO;
import static com.br.dbc.captacao.factory.GestorFactory.getGestorEntity;
import static com.br.dbc.captacao.factory.LinguagemFactory.getLinguagemDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GestorServiceTest {

    @InjectMocks
    private GestorService gestorService;

    @Mock
    private GestorRepository gestorRepository;

    @Mock
    private CargoService cargoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(gestorService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarListarComSucesso(){
        Integer pagina = 4;
        Integer quantidade = 6;
        String sort = "a";
        int order = 1;

        GestorEntity gestorEntity = getGestorEntity();
        Page<GestorEntity> gestorEntityPage = new PageImpl<>(List.of(gestorEntity));
        when(gestorRepository.findAll(any(Pageable.class))).thenReturn(gestorEntityPage);

        PageDTO<GestorDTO> gestorDTOPageDTO = gestorService.listar(pagina, quantidade, sort, order);

        assertNotNull(gestorDTOPageDTO);
    }

    @Test
    public void deveTestarFindDtoByIdComSucesso() throws RegraDeNegocioException {
        Integer id = 1;
        GestorEntity gestorEntity = getGestorEntity();

        when(gestorRepository.findById(anyInt())).thenReturn(Optional.of(gestorEntity));
        GestorDTO gestorDTO = gestorService.findDtoById(id);

        assertNotNull(gestorDTO);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        Integer id = 1;
        GestorEntity gestorEntity = getGestorEntity();
        when(gestorRepository.findById(id)).thenReturn(Optional.of(gestorEntity));

        GestorEntity gestor = gestorService.findById(id);

        assertNotNull(gestor);
        assertEquals(1, gestor.getIdGestor());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarFindByIdComErro() throws RegraDeNegocioException {
        Integer busca = 10;
        when(gestorRepository.findById(anyInt())).thenReturn(Optional.empty());

        GestorEntity gestorEntity = gestorService.findById(busca);

    }

    @Test
    public void deveTestarFindGestorbyNomeOrEmailComSucesso() throws RegraDeNegocioException {
        GestorEmailNomeCargoDTO gestorEmailNomeCargoDTO = GestorFactory.getGestorNomeEmailCargoDTO();
        CargoEntity cargoEntity = CargoFactory.getCargoEntity();
        List<GestorEntity> lista = new ArrayList<>();
        lista.add(getGestorEntity());
        when(cargoService.findById(anyInt())).thenReturn(cargoEntity);
        when(gestorRepository.findGestorEntitiesByCargoEntityAndNomeIgnoreCaseOrCargoEntityAndEmailIgnoreCase(any(),anyString(),any(),anyString())).thenReturn(lista);

        List<GestorDTO> gestorDTOList = gestorService.findGestorbyNomeOrEmail(gestorEmailNomeCargoDTO);

        assertNotNull(gestorDTOList);
    }

    @Test
    public void deveTestarRemoverComSucesso() throws RegraDeNegocioException {
        Integer id = 1;
        GestorEntity gestorEntity = getGestorEntity();
        when(gestorRepository.findById(anyInt())).thenReturn(Optional.of(gestorEntity));

        gestorService.remover(id);

        verify(gestorRepository, times(1)).deleteById(anyInt());

    }

    @Test
    public void deveTestarFindByEmailComSucesso() throws RegraDeNegocioException {
        String email = "dbc@dbccompany.com.br";
        GestorEntity gestorEntity = getGestorEntity();
        when(gestorRepository.findGestorEntityByEmailEqualsIgnoreCase(anyString())).thenReturn(Optional.of(gestorEntity));

        GestorEntity gestor = gestorService.findByEmail(email);

        assertNotNull(gestor);

    }

    @Test
    public void deveTestarDesativarContaComSucesso() throws RegraDeNegocioException {
        Integer id = 1;
        GestorEntity gestorEntity = getGestorEntity();
        when(gestorRepository.findById(anyInt())).thenReturn(Optional.of(gestorEntity));
        when(gestorRepository.save(any())).thenReturn(gestorEntity);

        GestorDTO gestorDTO = gestorService.desativarConta(id);

        assertNotNull(gestorDTO);
    }

    @Test
    public void deveTestarContasInativasComSucesso() {
        List<GestorEntity> gestorEntities = new ArrayList<>();
        gestorEntities.add(getGestorEntity());
        when(gestorRepository.findByAtivo(any())).thenReturn(gestorEntities);

        List<GestorDTO> gestorDTOS = gestorService.contasInativas();

        assertNotNull(gestorDTOS);
    }

    @Test
    public void deveConverterGestorDTOParaEntityComSucesso() {
        //Setup
        GestorDTO gestorDTO = getGestorDTO();

        // Act

        GestorEntity gestorEntity = gestorService.convertToEntity(gestorDTO);
        //Assert
        assertEquals(gestorEntity.getNome(), gestorDTO.getNome());
    }


}
