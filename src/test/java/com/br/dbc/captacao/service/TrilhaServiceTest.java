package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.trilha.TrilhaCreateDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.TrilhaRepository;
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

import java.util.*;

import static com.br.dbc.captacao.factory.TrilhaFactory.getTrilhaDTO;
import static com.br.dbc.captacao.factory.TrilhaFactory.getTrilhaEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrilhaServiceTest {
    @InjectMocks
    private TrilhaService trilhaService;

    @Mock
    private TrilhaRepository trilhaRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(trilhaService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCriarComSucesso() throws RegraDeNegocioException {
    final String trilhaNova = "Fullstack";
    TrilhaEntity trilha = getTrilhaEntity();
    TrilhaCreateDTO trilhaCreateDTO = getTrilhaDTO();
    trilhaCreateDTO.setNome("Fullstack");

    when(trilhaRepository.save(any())).thenReturn(trilha);

    TrilhaDTO novaTrilha = trilhaService.create(trilhaCreateDTO);

    assertEquals(trilhaNova, novaTrilha.getNome());

    }

    @Test
    public void deveListarComSucesso(){
        TrilhaEntity trilha = getTrilhaEntity();

        when(trilhaRepository.findAll()).thenReturn(List.of(trilha));

        List<TrilhaDTO> listarTrilha = trilhaService.list();

        assertEquals(1, listarTrilha.size());
    }

    @Test
    public void deveListaTrilhasComSucesso() throws RegraDeNegocioException{
        TrilhaEntity trilha = getTrilhaEntity();
        List<TrilhaEntity> trilhaEntities = new ArrayList<>();
        trilhaEntities.add(trilha);
        Set<TrilhaEntity> trilhaEntities1 = new HashSet<>();
        trilhaEntities1.add(trilha);

        List<Integer> listaIds = new ArrayList<>();
        listaIds.add(trilha.getIdTrilha());

        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.of(trilha));

        Set<TrilhaEntity> novasTrilhas = trilhaService.findListaTrilhas(listaIds);

        assertEquals(1, novasTrilhas.size());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveListaTrilhasComErro() throws RegraDeNegocioException{
        TrilhaEntity trilha = getTrilhaEntity();
        List<TrilhaEntity> trilhaEntities = new ArrayList<>();
        trilhaEntities.add(trilha);
        Set<TrilhaEntity> trilhaEntities1 = new HashSet<>();
        trilhaEntities1.add(trilha);

        List<Integer> listaIds = new ArrayList<>();
        listaIds.add(trilha.getIdTrilha());

       trilhaService.findListaTrilhas(listaIds);
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        Integer id = 10;

        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.of(getTrilhaEntity()));
        trilhaService.deleteFisico(id);

        verify(trilhaRepository, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarDeleteComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.empty());
        trilhaService.deleteFisico(id);
    }

    @Test
    public void deveConverterParaEntityComSucesso() throws RegraDeNegocioException{
        TrilhaDTO trilhaDTO = getTrilhaDTO();
        Set<TrilhaDTO> trilhaDTOS = new HashSet<>();
        trilhaDTOS.add(trilhaDTO);

        Set<TrilhaEntity> trilhaEntity = trilhaService.convertToEntity(trilhaDTOS);

        assertEquals(1, trilhaEntity.size());
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        Integer id = 1;

        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.of(getTrilhaEntity()));
        TrilhaEntity trilhaEntity = trilhaService.findById(id);

        assertNotNull(trilhaEntity);
        assertEquals(id, trilhaEntity.getIdTrilha());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByIdComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.empty());
        trilhaService.findById(id);
    }

    @Test
    public void deveTestarFindByNomeComSucesso() throws RegraDeNegocioException {
        String nome = "BACKEND";

        when(trilhaRepository.findByNome(anyString())).thenReturn(Optional.of(getTrilhaEntity()));
        TrilhaEntity trilhaEntity = trilhaService.findByNome(nome);

        assertNotNull(trilhaEntity);
        assertEquals(trilhaEntity.getNome(), nome);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByNomeComErro() throws RegraDeNegocioException {
        String nome = "QA";
        when(trilhaRepository.findByNome(anyString())).thenReturn(Optional.empty());
        trilhaService.findByNome(nome);
    }
}
