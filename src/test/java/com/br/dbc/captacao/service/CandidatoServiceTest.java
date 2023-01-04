//package com.br.dbc.captacao.service;
//
//import com.br.dbc.captacao.dto.candidato.CandidatoCreateDTO;
//import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
//import com.br.dbc.captacao.dto.formulario.FormularioDTO;
//import com.br.dbc.captacao.entity.CandidatoEntity;
//import com.br.dbc.captacao.entity.EdicaoEntity;
//import com.br.dbc.captacao.entity.FormularioEntity;
//import com.br.dbc.captacao.entity.LinguagemEntity;
//import com.br.dbc.captacao.enums.TipoMarcacao;
//import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
//import com.br.dbc.captacao.exception.RegraDeNegocioException;
//import com.br.dbc.captacao.factory.CandidatoFactory;
//import com.br.dbc.captacao.factory.EdicaoFactory;
//import com.br.dbc.captacao.factory.FormularioFactory;
//import com.br.dbc.captacao.factory.LinguagemFactory;
//import com.br.dbc.captacao.repository.CandidatoRepository;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CandidatoServiceTest {
//
//    @InjectMocks
//    private CandidatoService candidatoService;
//
//    @Mock
//    private CandidatoRepository candidatoRepository;
//
//    @Mock
//    private EdicaoService edicaoService;
//
//    @Mock
//    private FormularioService formularioService;
//
//    @Mock
//    private LinguagemService linguagemService;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ReflectionTestUtils.setField(candidatoService, "objectMapper", objectMapper);
//    }
//    @Test
//    public void deveTestarCreateComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
//
//        CandidatoCreateDTO candidatoCreateDTO = CandidatoFactory.getCandidatoCreateDTO();
//        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();
//        FormularioDTO formularioDTO = FormularioFactory.getFormularioDto();
//        LinguagemEntity linguagemEntity = LinguagemFactory.getLinguagemEntity();
//
//        when(candidatoRepository.findByEmail(anyString()))
//                .thenReturn(Optional.empty());
//
//        when(linguagemService.findByNome(anyString()))
//                .thenReturn(linguagemEntity);
//
//        when(edicaoService.findByNome(anyString()))
//                .thenReturn(edicaoEntity);
//
//        when(formularioService.findDtoById(anyInt()))
//                .thenReturn(formularioDTO);
//
//        when(formularioService.convertToEntity(any(FormularioDTO.class)))
//                .thenReturn(FormularioFactory.getFormularioEntity());
//
//        when(formularioService.convertToDto(any(FormularioEntity.class)))
//                .thenReturn(formularioDTO);
//
//       CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
//
//       assertNotNull(candidatoDTO);
//       assertEquals(candidatoCreateDTO.getEmail(),candidatoDTO.getEmail());
//
//    }
//
//
//}
