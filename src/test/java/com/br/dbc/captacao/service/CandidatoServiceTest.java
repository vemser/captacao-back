package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.CandidatoCreateDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.entity.LinguagemEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.*;
import com.br.dbc.captacao.repository.CandidatoRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CandidatoServiceTest {

    @InjectMocks
    private CandidatoService candidatoService;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private EdicaoService edicaoService;

    @Mock
    private FormularioService formularioService;

    @Mock
    private LinguagemService linguagemService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(candidatoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCreateComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {

        CandidatoCreateDTO candidatoCreateDTO = CandidatoFactory.getCandidatoCreateDTO();
        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();
        FormularioDTO formularioDTO = FormularioFactory.getFormularioDto();
        LinguagemEntity linguagemEntity = LinguagemFactory.getLinguagemEntity();

        when(candidatoRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(linguagemService.findByNome(anyString()))
                .thenReturn(linguagemEntity);

        when(edicaoService.findByNome(anyString()))
                .thenReturn(edicaoEntity);

        when(candidatoRepository.save(any(CandidatoEntity.class)))
                .thenReturn(CandidatoFactory.getCandidatoEntity());

        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);

        assertNotNull(candidatoDTO);
        assertEquals(candidatoCreateDTO.getEmail(), candidatoDTO.getEmail());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateComCandidatoQueExisteRetornandoRegraDeNegocioExcpetion() throws RegraDeNegocioException, RegraDeNegocio404Exception {

        CandidatoCreateDTO candidatoCreateDTO = CandidatoFactory.getCandidatoCreateDTO();

        when(candidatoRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(CandidatoFactory.getCandidatoEntity()));

        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateComEmailInvalidoRetornandoRegraDeNegocioExcpetion() throws RegraDeNegocioException, RegraDeNegocio404Exception {

        CandidatoCreateDTO candidatoCreateDTO = new CandidatoCreateDTO();
        candidatoCreateDTO.setEmail("");

        when(candidatoRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
    }

    @Test
    public void deveTestarListaAllPaginadoComSucesso() throws RegraDeNegocioException {
        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idCandidato";
        Integer orderDescendente = 1;
        Sort ordenacao = Sort.by(sort).descending();

        PageImpl<CandidatoEntity> candidatoEntities = new PageImpl<>(List.of(CandidatoFactory.getCandidatoEntity()),
                PageRequest.of(pagina, tamanho, ordenacao), 0);

        when(candidatoRepository.findAll(any(Pageable.class)))
                .thenReturn(candidatoEntities);

        PageDTO<CandidatoDTO> pages = candidatoService.listaAllPaginado(pagina, tamanho, sort, orderDescendente);

        assertEquals(pages.getTamanho(), tamanho);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarListaAllPaginadoComTamanhoMenorOuIgualAZeroComException() throws RegraDeNegocioException {
        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idCandidato";
        Integer orderDescendente = 1;
        Sort ordenacao = Sort.by(sort).descending();

        PageImpl<CandidatoEntity> candidatoEntities = new PageImpl<>(List.of(CandidatoFactory.getCandidatoEntity()),
                PageRequest.of(pagina, tamanho, ordenacao), 0);

        PageDTO<CandidatoDTO> pages = candidatoService.listaAllPaginado(pagina, 0, sort, orderDescendente);
    }

    @Test
    public void deveTestarListaAllPaginadoComCandidatoComImagemComSucesso() throws RegraDeNegocioException {
        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idCandidato";
        Integer orderDescendente = 1;
        Sort ordenacao = Sort.by(sort).descending();

        PageImpl<CandidatoEntity> candidatoEntities = new PageImpl<>(List.of(CandidatoFactory.getCandidatoEntity()),
                PageRequest.of(pagina, tamanho, ordenacao), 0);

        candidatoEntities.stream()
                .map(candidatoEntity -> {
                    candidatoEntity.setImageEntity(ImageFactory.getImageEntity());
                    return candidatoEntity;
                }).toList();

        when(candidatoRepository.findAll(any(Pageable.class)))
                .thenReturn(candidatoEntities);

        PageDTO<CandidatoDTO> pages = candidatoService.listaAllPaginado(pagina, tamanho, sort, orderDescendente);

        assertEquals(pages.getTamanho(), tamanho);
    }

    @Test
    public void deveTestarDeleteLogicoById() throws RegraDeNegocioException {

        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

       candidatoService.deleteLogicoById(1);

       assertNotEquals(candidatoEntity.getAtivo(), TipoMarcacao.T);
    }

    @Test
    public void deveTestarDeleteFisico() throws RegraDeNegocioException {

        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

        candidatoService.deleteLogicoById(1);
    }

//    @Test
//    public void deveTestarUpdateComSucesso(){
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//    }



}
