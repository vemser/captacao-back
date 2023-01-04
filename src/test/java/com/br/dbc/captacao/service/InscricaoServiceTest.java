//package com.br.dbc.captacao.service;
//
//
//import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
//import com.br.dbc.captacao.dto.formulario.FormularioCreateDTO;
//import com.br.dbc.captacao.dto.formulario.FormularioDTO;
//import com.br.dbc.captacao.dto.inscricao.InscricaoCreateDTO;
//import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
//import com.br.dbc.captacao.dto.paginacao.PageDTO;
//import com.br.dbc.captacao.entity.*;
//import com.br.dbc.captacao.enums.TipoMarcacao;
//import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
//import com.br.dbc.captacao.exception.RegraDeNegocioException;
//import com.br.dbc.captacao.factory.CandidatoFactory;
//import com.br.dbc.captacao.factory.FormularioFactory;
//import com.br.dbc.captacao.factory.InscricaoFactory;
//import com.br.dbc.captacao.factory.TrilhaFactory;
//import com.br.dbc.captacao.repository.CandidatoRepository;
//import com.br.dbc.captacao.repository.FormularioRepository;
//import com.br.dbc.captacao.repository.InscricaoRepository;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class InscricaoServiceTest {
//
//    @InjectMocks
//    private InscricaoService inscricaoService;
//
//    @Mock
//    private InscricaoRepository inscricaoRepository;
//
//    @Mock
//    private CandidatoRepository candidatoRepository;
//
//    @InjectMocks
//    private CandidatoService candidatoService;
//
//    @InjectMocks
//    private EdicaoService edicaoService;
//
//    @InjectMocks
//    private TrilhaService trilhaService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ReflectionTestUtils.setField(inscricaoService, "objectMapper", objectMapper);
//    }
//
//    @Test
//    public void deveTestarCreateInscricaoComSucesso() throws RegraDeNegocioException {
//        InscricaoCreateDTO inscricaoCreateDTO = InscricaoFactory.getInscricaoCreateDto();
//
//
//        InscricaoEntity inscricaoEntity = new InscricaoEntity();
//        inscricaoEntity.setIdInscricao(1);
//        inscricaoEntity.setDataInscricao(LocalDate.now());
//        inscricaoEntity.setAvaliado(TipoMarcacao.T);
//        when(inscricaoRepository.save(any())).thenReturn(inscricaoEntity);
//
//
//        InscricaoDTO inscricaoDTORetorno = inscricaoService.create(inscricaoCreateDTO.getIdCandidato());
//
//        assertNotNull(inscricaoDTORetorno);
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveTestarCreateInscricaoComException() throws RegraDeNegocioException {
//
//        InscricaoCreateDTO inscricaoCreateDTO = InscricaoFactory.getInscricaoCreateDto();
//        inscricaoCreateDTO.setIdCandidato(1);
//
//        inscricaoService.create(inscricaoCreateDTO.getIdCandidato());
//
//        verify(inscricaoRepository, times(1)).save(any());
//    }
//
//    @Test
//    public void deveTestarSetAvaliadoComSucesso () {
//        InscricaoEntity inscricaoEntity = new InscricaoEntity();
//        inscricaoEntity.setAvaliado(TipoMarcacao.F);
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        candidatoEntity.setIdCandidato(1);
//
//        when(candidatoRepository.findById(anyInt())).thenReturn(Optional.of(candidatoEntity));
//
//        inscricaoRepository.save(inscricaoEntity);
//
//        verify(inscricaoRepository, times(1)).save(any());
//    }
//
//    @Test
//    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
//        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
//
//        when(inscricaoRepository.findById(anyInt())).thenReturn(Optional.of(inscricaoEntity));
//
//        inscricaoService.delete(1);
//
//        verify(inscricaoRepository, times(1)).deleteById(anyInt());
//    }
//
//    @Test
//    public void deveTestarListarComSucesso() throws RegraDeNegocioException {
//
//        Integer pagina = 1;
//        Integer tamanho = 1;
//        String sort = "idInscricao";
//        int orderDescendente = 0;
//
//        Sort ordenacao = Sort.by(sort).descending();
//
//        PageImpl<InscricaoEntity> pageImpl = new PageImpl<>(List.of(InscricaoFactory.getInscricaoEntity()),
//                PageRequest.of(pagina, tamanho, ordenacao), 0);
//        pageImpl.stream()
//                .map(inscricaoEntity -> {
//                    inscricaoService.converterParaDTO(inscricaoEntity);
//                    return inscricaoEntity;
//                }).toList();
//
//        PageDTO<InscricaoDTO> page = inscricaoService.listar(pagina, 0, sort, orderDescendente);
//
//        assertEquals(page.getTamanho(), 0);
//    }
//
//    @Test
//    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
//        Integer busca = 10;
//
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        candidatoEntity.setIdCandidato(1);
//        when(candidatoRepository.findById(anyInt())).thenReturn(Optional.of(candidatoEntity));
//
//        CandidatoEntity candidatoRecuperado = candidatoService.findById(busca);
//
//        assertNotNull(candidatoRecuperado);
//        assertEquals(1, candidatoRecuperado.getIdCandidato());
//    }
//
//    @Test
//    public void deveTestarFindDtoByIdComSucesso() throws RegraDeNegocioException {
//        Integer busca = 10;
//
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        candidatoEntity.setIdCandidato(1);
//        when(candidatoRepository.findById(anyInt())).thenReturn(Optional.of(candidatoEntity));
//
//        CandidatoDTO candidatoRecuperado = candidatoService.findDtoById(busca);
//
//        assertNotNull(candidatoRecuperado);
//        assertEquals(1, candidatoRecuperado.getIdCandidato());
//    }
//
//    @Test
//    public void deveTestarConvertToEntity() throws RegraDeNegocioException, RegraDeNegocio404Exception {
//
//        InscricaoDTO inscricaoDTO = InscricaoFactory.getInscricaoDto();
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();
//        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
//        Set<TrilhaEntity> trilhas = new HashSet<>();
//        trilhas.add(trilhaEntity);
//
//        when(candidatoService.convertToEntity(inscricaoDTO.getCandidato())).thenReturn(candidatoEntity);
//
//        InscricaoEntity inscricaoEntityRetorno = inscricaoService.convertToEntity(inscricaoDTO);
//
//        Assert.assertNotNull(inscricaoEntityRetorno);
//    }
//
//    @Test
//    public void deveTestarConverterParaDTO() throws RegraDeNegocio404Exception, RegraDeNegocioException {
//        InscricaoDTO inscricaoDTO = InscricaoFactory.getInscricaoDto();
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();
//        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
//        Set<TrilhaEntity> trilhas = new HashSet<>();
//        trilhas.add(trilhaEntity);
//
//        when(candidatoService.convertToEntity(inscricaoDTO.getCandidato())).thenReturn(candidatoEntity);
//
//        InscricaoEntity inscricaoEntityRetorno = inscricaoService.convertToEntity(inscricaoDTO);
//
//        Assert.assertNotNull(inscricaoEntityRetorno);
//    }
//}
