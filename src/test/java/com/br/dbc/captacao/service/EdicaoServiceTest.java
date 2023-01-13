package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.EdicaoRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.br.dbc.captacao.factory.EdicaoFactory.getEdicaoDTO;
import static com.br.dbc.captacao.factory.EdicaoFactory.getEdicaoEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EdicaoServiceTest {

    @InjectMocks
    private EdicaoService edicaoService;

    @Mock
    private EdicaoRepository edicaoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(edicaoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveRetornarEdicaoQuandoEdicaoEstiverCadastradaNoBancoPeloIDComSucesso() throws RegraDeNegocioException {
        // SETUP
        final int ID_EDICAO_ESPERADO = 2;
        final String nome = "10";
        EdicaoEntity edicaoEsperada = new EdicaoEntity();
        edicaoEsperada.setIdEdicao(ID_EDICAO_ESPERADO);
        edicaoEsperada.setNome(nome);

        // ACT
        when(edicaoRepository.findById(ID_EDICAO_ESPERADO)).thenReturn(Optional.of(edicaoEsperada));
        EdicaoEntity edicaoResponse = edicaoService.findById(ID_EDICAO_ESPERADO);

        // ASSERT
        assertEquals(ID_EDICAO_ESPERADO, edicaoResponse.getIdEdicao());
        assertEquals(nome, edicaoEsperada.getNome());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveCriarUmaNovaEdicaoCasoNaoExistaONomeNoBanco() throws RegraDeNegocioException {
        final String edicaoNome = "Edição 10";
        EdicaoEntity edicao = getEdicaoEntity();

        when(edicaoRepository.findByNome(anyString())).thenReturn(Optional.empty());

        EdicaoEntity edicaoRetornada = edicaoService.findByNome(edicaoNome);

    }

    @Test
    public void deveRetornarEdicaoQuandoEdicaoEstiverCadastradaNoBancoPeloNomeComSucesso() throws RegraDeNegocioException {
        //Setup
        String nome = "Edição 10";

        final int ID_EDICAO_ESPERADO = 2;
        EdicaoEntity edicaoEsperada = new EdicaoEntity();
        edicaoEsperada.setNome(nome);
        edicaoEsperada.setIdEdicao(ID_EDICAO_ESPERADO);
        // Act
        when(edicaoRepository.findByNome(anyString())).thenReturn(Optional.of(edicaoEsperada));
        EdicaoEntity edicaoResponse = edicaoService.findByNome(nome);

        // Assert
        assertEquals(nome, edicaoResponse.getNome());
    }

    @Test
    public void deveTestarConverterEdicaoDTOParaEntityComSucesso() {
        //Setup
        EdicaoDTO edicao = getEdicaoDTO();

        //Act
        EdicaoEntity edicaoEntityResponse = edicaoService.converterEntity(edicao);
        //Assert
        assertEquals(edicaoEntityResponse.getNome(), edicao.getNome());
    }

    @Test
    public void deveTestarDeleteFisicoComSucesso() throws RegraDeNegocioException {
        //Setup
        final int idEdicao = 1;
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        // Act
        when(edicaoRepository.findById(idEdicao)).thenReturn(Optional.of(edicaoEntity));
        edicaoService.deleteFisico(edicaoEntity.getIdEdicao());

        //Assert
        verify(edicaoRepository).deleteById(idEdicao);
    }

//    @Test
//    public void testarCriarEdicaoCasoNaoExistaUmaComSucesso() {
//        //Setup
//        final String nome = "12";
//        EdicaoDTO edicaoDTO = new EdicaoDTO();
//        edicaoDTO.setNome(nome);
//        final int id = 2;
//        EdicaoEntity edicaoEntity = new EdicaoEntity();
//        edicaoEntity.setNome(nome);
//        edicaoEntity.setIdEdicao(id);
//        //Act
//        when(edicaoRepository.save(any())).thenReturn(edicaoEntity);
//        EdicaoDTO edicaoEntityResponse = edicaoService.createAndReturnDTO(edicaoDTO);
//        //Assert
//        assertEquals(edicaoEntity.getNome(), edicaoEntityResponse.getNome());
//    }

//    @Test
//    public void deveTestarCriacaoComSucesso() {
//        // Setup
//        EdicaoDTO edicaoDTO = getEdicaoDTO();
//        EdicaoEntity edicaoEntity = getEdicaoEntity();
//        CandidatoEntity candidatoEntity = new CandidatoEntity();
//        Set<CandidatoEntity> lista = new HashSet<>();
//        lista.add(candidatoEntity);
//        edicaoEntity.setCandidatoEntities(lista);
//        // Act
//        when(edicaoRepository.save(any())).thenReturn(edicaoEntity);
//        EdicaoDTO edicaoDTOResponse = edicaoService.createAndReturnDTO(edicaoDTO);
//        // Assert
//        assertEquals(edicaoEntity.getNome(), edicaoDTOResponse.getNome());
//    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarBuscaEdicaoPorIdComErro() throws RegraDeNegocioException {
        final int id = 1;
        when(edicaoRepository.findById(id)).thenReturn(Optional.empty());
        edicaoService.findById(id);
    }

    @Test
    public void deveRetornarEdiçãoAtual() throws RegraDeNegocioException {
        String edicaoNomeEsperado = "Edição 10";
        EdicaoEntity edicaoRetornada = new EdicaoEntity(1, edicaoNomeEsperado, Set.of());

        when(edicaoRepository.findAll()).thenReturn(List.of(edicaoRetornada));
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoRetornada));
        String edicaoAtualNome = edicaoService.retornarEdicaoAtual();

        assertEquals(edicaoNomeEsperado, edicaoAtualNome);
    }

    @Test
    public void deveRetornarListaDeEdicoes() {
        final int tamanhoEsperado = 1;
        List<EdicaoEntity> list = List.of(getEdicaoEntity());

        when(edicaoRepository.findAll()).thenReturn(list);
        List<EdicaoDTO> edicoesList = edicaoService.list();

        assertEquals(tamanhoEsperado, edicoesList.size());
    }
}
