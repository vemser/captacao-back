<<<<<<< HEAD
package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.*;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO;
import com.br.dbc.captacao.entity.*;
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.MockUtil.createMock;

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
    private TrilhaService trilhaService;

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
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);

        when(candidatoRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(linguagemService.findByNome(anyString()))
                .thenReturn(linguagemEntity);

        when(edicaoService.findByNome(anyString()))
                .thenReturn(edicaoEntity);

        when(candidatoRepository.save(any(CandidatoEntity.class)))
                .thenReturn(candidatoEntity);

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
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);

        PageImpl<CandidatoEntity> candidatoEntities = new PageImpl<>(List.of(candidatoEntity),
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
        CandidatoEntity candidato = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidato.setFormularioEntity(formulario);

        PageImpl<CandidatoEntity> candidatoEntities = new PageImpl<>(List.of(candidato),
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

        candidatoService.deleteFisico(1);
    }

    @Test
    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);
        CandidatoCreateDTO candidatoCreateDTO = CandidatoFactory.getCandidatoCreateDTO();
        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

        when(edicaoService.findByNome(anyString()))
                .thenReturn(edicaoEntity);

        when(candidatoRepository.save(any(CandidatoEntity.class)))
                .thenReturn(candidatoEntity);

        CandidatoDTO candidatoDTO = candidatoService.update(1, candidatoCreateDTO);

        assertEquals(candidatoDTO.getNome(), candidatoEntity.getNome());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateComException() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        CandidatoCreateDTO candidatoCreateDTO = CandidatoFactory.getCandidatoCreateDTO();
        candidatoCreateDTO.setEmail("");
        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

        CandidatoDTO candidatoDTO = candidatoService.update(1, candidatoCreateDTO);
    }

    @Test
    public void deveTestarUpdateTecnicoComSucesso() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);
        CandidatoTecnicoNotaDTO candidatoTecnicoNotaDTO = new CandidatoTecnicoNotaDTO();
        candidatoTecnicoNotaDTO.setNotaTecnico(80.0);
        candidatoTecnicoNotaDTO.setParecerTecnico("observacao");

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

        when(candidatoRepository.save(any(CandidatoEntity.class)))
                .thenReturn(candidatoEntity);

        CandidatoDTO candidatoRetorno = candidatoService.updateTecnico(1, candidatoTecnicoNotaDTO);

        assertNotNull(candidatoRetorno);
    }

    @Test
    public void deveTestarCalcularMediaNotas() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        candidatoEntity.setNotaProva(80.0);
        candidatoEntity.setNotaEntrevistaTecnica(90.0);
        candidatoEntity.setNotaEntrevistaComportamental(70.0);
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

        when(candidatoRepository.save(any(CandidatoEntity.class)))
                .thenReturn(candidatoEntity);

        CandidatoDTO candidatoDTO = candidatoService.calcularMediaNotas(1);

        assertNotNull(candidatoDTO);
    }


    @Test
    public void deveTestarUpdateNota() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);
        CandidatoNotaDTO candidatoNotaDTO = new CandidatoNotaDTO();
        candidatoNotaDTO.setNotaProva(80.0);

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

        when(candidatoRepository.save(any(CandidatoEntity.class)))
                .thenReturn(candidatoEntity);

        CandidatoDTO candidatoRetorno = candidatoService.updateNota(1, candidatoNotaDTO);

        assertNotNull(candidatoRetorno);
    }

    @Test
    public void deveTestarUpdateComportamental() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);
        CandidatoNotaComportamentalDTO candidatoNotaComportamentalDTO = new CandidatoNotaComportamentalDTO();
        candidatoNotaComportamentalDTO.setParecerComportamental("observacoes");
        candidatoNotaComportamentalDTO.setNotaComportamental(80.0);

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

        when(candidatoRepository.save(any(CandidatoEntity.class)))
                .thenReturn(candidatoEntity);

        CandidatoDTO candidatoDTO = candidatoService.updateComportamental(1, candidatoNotaComportamentalDTO);

        assertNotNull(candidatoDTO);
    }

    @Test
    public void deveTestarFindDtoByIdComSucesso() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);

        when(candidatoRepository.findById(anyInt()))
                .thenReturn(Optional.of(candidatoEntity));

        CandidatoDTO candidatoDTO = candidatoService.findDtoById(1);

        assertEquals(candidatoDTO.getNome(), candidatoEntity.getNome());
    }

    @Test
    public void deveTestarFindByEmailComSucesso() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);

        when(candidatoRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(candidatoEntity));

        CandidatoDTO candidatoDTO = candidatoService.findByEmail("email@email");

        assertEquals(candidatoDTO.getNome(), candidatoEntity.getNome());
    }

    @Test
    public void deveTestarFindCandidatoDtoByEmailComSucesso() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        candidatoEntity.setFormularioEntity(formulario);

        when(candidatoRepository.findCandidatoEntitiesByEmail(anyString()))
                .thenReturn(candidatoEntity);

        CandidatoDTO candidatoDTO = candidatoService.findCandidatoDtoByEmail("email@email");

        assertEquals(candidatoDTO.getNome(), candidatoEntity.getNome());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarFindByEmailEntityComException() throws RegraDeNegocioException {

        when(candidatoRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        candidatoService.findByEmailEntity("email@email");
    }

    @Test
    public void deveTestarConvertToEntityComSucesso() throws RegraDeNegocio404Exception, RegraDeNegocioException {

        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
        candidatoDTO.setFormulario(FormularioFactory.getFormularioDto());

        when(formularioService.findById(anyInt()))
                .thenReturn(FormularioFactory.getFormularioEntity());

        CandidatoEntity candidatoRetorno = candidatoService.convertToEntity(candidatoDTO);

        assertEquals(candidatoDTO.getNome(), candidatoRetorno.getNome());
    }

    @Test
    public void deveTestarListCandidatosByTrilhaComSucesso() throws RegraDeNegocioException {
        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(new HashSet<>(List.of(trilhaEntity)));
        candidatoEntity.setFormularioEntity(formulario);
        List<CandidatoEntity> listCandidato = new ArrayList<>();
        listCandidato.add(candidatoEntity);

        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
        List<CandidatoDTO> candidatoDTOList = new ArrayList<>();
        candidatoDTOList.add(candidatoDTO);

        when(trilhaService.findByNome(anyString()))
                .thenReturn(trilhaEntity);

        when(candidatoRepository.findCandidatoEntitiesByFormularioEntity_TrilhaEntitySet(any(TrilhaEntity.class)))
                .thenReturn(listCandidato);

        List<CandidatoDTO> listaRetorno = candidatoService.listCandidatosByTrilha("frontend");

        assertNotNull(listaRetorno);

    }

    @Test
    public void deveTestarListCandidatosByEdicao() throws RegraDeNegocioException {

        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();
        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(new HashSet<>(List.of(trilhaEntity)));
        candidatoEntity.setFormularioEntity(formulario);
        List<CandidatoEntity> listCandidato = new ArrayList<>();
        listCandidato.add(candidatoEntity);

        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
        List<CandidatoDTO> candidatoDTOList = new ArrayList<>();
        candidatoDTOList.add(candidatoDTO);

        when(edicaoService.findByNome(anyString()))
                .thenReturn(edicaoEntity);

        when(candidatoRepository.findCandidatoEntitiesByEdicao(any(EdicaoEntity.class)))
                .thenReturn(listCandidato);

        List<CandidatoDTO> listaRetorno = candidatoService.listCandidatosByEdicao("VemSer12");

        assertNotNull(listaRetorno);
    }

    @Test
    public void deveTestarExportarCandidatoCSVComSucesso() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        List<CandidatoEntity> candidatoEntityList = new ArrayList<>();
        candidatoEntityList.add(candidatoEntity);

        BufferedWriter writer = mock(BufferedWriter.class);
        OutputStreamWriter streamWriter = mock(OutputStreamWriter.class);
        FileOutputStream fileOutputStream=mock(FileOutputStream.class);

        when(candidatoRepository.findAll())
                .thenReturn(candidatoEntityList);

        candidatoService.exportarCandidatoCSV();
    }

    @Test
    public void deveTestarListCandidatosByNotaComSucesso() {

        Sort orderBy = Sort.by("notaProva");
        PageRequest pageRequest = PageRequest.of(0, 1, orderBy);
        Page<CandidatoEntity> page = mock(Page.class);

        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
        List<CandidatoDTO> candidatoDTOList = new ArrayList<>();
        candidatoDTOList.add(candidatoDTO);

        when(candidatoRepository.findByNota(any(PageRequest.class)))
                .thenReturn(page);

        PageDTO<CandidatoDTO> listCandidatoPage = candidatoService.listCandidatosByNota(0,1);

        assertNotNull(listCandidatoPage);
    }
}
=======
//package com.br.dbc.captacao.service;
//
//import com.br.dbc.captacao.dto.candidato.*;
//import com.br.dbc.captacao.dto.formulario.FormularioDTO;
//import com.br.dbc.captacao.dto.paginacao.PageDTO;
//import com.br.dbc.captacao.entity.*;
//import com.br.dbc.captacao.enums.TipoMarcacao;
//import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
//import com.br.dbc.captacao.exception.RegraDeNegocioException;
//import com.br.dbc.captacao.factory.*;
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
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.domain.*;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.mockito.internal.util.MockUtil.createMock;
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
//    private TrilhaService trilhaService;
//
//    @Mock
//    private LinguagemService linguagemService;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ReflectionTestUtils.setField(candidatoService, "objectMapper", objectMapper);
//    }
//
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
//        when(candidatoRepository.save(any(CandidatoEntity.class)))
//                .thenReturn(CandidatoFactory.getCandidatoEntity());
//
//        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
//
//        assertNotNull(candidatoDTO);
//        assertEquals(candidatoCreateDTO.getEmail(), candidatoDTO.getEmail());
//
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveTestarCreateComCandidatoQueExisteRetornandoRegraDeNegocioExcpetion() throws RegraDeNegocioException, RegraDeNegocio404Exception {
//
//        CandidatoCreateDTO candidatoCreateDTO = CandidatoFactory.getCandidatoCreateDTO();
//
//        when(candidatoRepository.findByEmail(anyString()))
//                .thenReturn(Optional.of(CandidatoFactory.getCandidatoEntity()));
//
//        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveTestarCreateComEmailInvalidoRetornandoRegraDeNegocioExcpetion() throws RegraDeNegocioException, RegraDeNegocio404Exception {
//
//        CandidatoCreateDTO candidatoCreateDTO = new CandidatoCreateDTO();
//        candidatoCreateDTO.setEmail("");
//
//        when(candidatoRepository.findByEmail(anyString()))
//                .thenReturn(Optional.empty());
//
//        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
//    }
//
//    @Test
//    public void deveTestarListaAllPaginadoComSucesso() throws RegraDeNegocioException {
//        Integer pagina = 1;
//        Integer tamanho = 5;
//        String sort = "idCandidato";
//        Integer orderDescendente = 1;
//        Sort ordenacao = Sort.by(sort).descending();
//
//        PageImpl<CandidatoEntity> candidatoEntities = new PageImpl<>(List.of(CandidatoFactory.getCandidatoEntity()),
//                PageRequest.of(pagina, tamanho, ordenacao), 0);
//
//        when(candidatoRepository.findAll(any(Pageable.class)))
//                .thenReturn(candidatoEntities);
//
//        PageDTO<CandidatoDTO> pages = candidatoService.listaAllPaginado(pagina, tamanho, sort, orderDescendente);
//
//        assertEquals(pages.getTamanho(), tamanho);
//
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveTestarListaAllPaginadoComTamanhoMenorOuIgualAZeroComException() throws RegraDeNegocioException {
//        Integer pagina = 1;
//        Integer tamanho = 5;
//        String sort = "idCandidato";
//        Integer orderDescendente = 1;
//        Sort ordenacao = Sort.by(sort).descending();
//
//        PageImpl<CandidatoEntity> candidatoEntities = new PageImpl<>(List.of(CandidatoFactory.getCandidatoEntity()),
//                PageRequest.of(pagina, tamanho, ordenacao), 0);
//
//        PageDTO<CandidatoDTO> pages = candidatoService.listaAllPaginado(pagina, 0, sort, orderDescendente);
//    }
//
//    @Test
//    public void deveTestarListaAllPaginadoComCandidatoComImagemComSucesso() throws RegraDeNegocioException {
//        Integer pagina = 1;
//        Integer tamanho = 5;
//        String sort = "idCandidato";
//        Integer orderDescendente = 1;
//        Sort ordenacao = Sort.by(sort).descending();
//
//        PageImpl<CandidatoEntity> candidatoEntities = new PageImpl<>(List.of(CandidatoFactory.getCandidatoEntity()),
//                PageRequest.of(pagina, tamanho, ordenacao), 0);
//
//        candidatoEntities.stream()
//                .map(candidatoEntity -> {
//                    candidatoEntity.setImageEntity(ImageFactory.getImageEntity());
//                    return candidatoEntity;
//                }).toList();
//
//        when(candidatoRepository.findAll(any(Pageable.class)))
//                .thenReturn(candidatoEntities);
//
//        PageDTO<CandidatoDTO> pages = candidatoService.listaAllPaginado(pagina, tamanho, sort, orderDescendente);
//
//        assertEquals(pages.getTamanho(), tamanho);
//    }
//
//    @Test
//    public void deveTestarDeleteLogicoById() throws RegraDeNegocioException {
//
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        candidatoService.deleteLogicoById(1);
//
//        assertNotEquals(candidatoEntity.getAtivo(), TipoMarcacao.T);
//    }
//
//    @Test
//    public void deveTestarDeleteFisico() throws RegraDeNegocioException {
//
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        candidatoService.deleteFisico(1);
//    }
//
//    @Test
//    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        CandidatoCreateDTO candidatoCreateDTO = CandidatoFactory.getCandidatoCreateDTO();
//        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        when(edicaoService.findByNome(anyString()))
//                .thenReturn(edicaoEntity);
//
//        when(candidatoRepository.save(any(CandidatoEntity.class)))
//                .thenReturn(candidatoEntity);
//
//        CandidatoDTO candidatoDTO = candidatoService.update(1, candidatoCreateDTO);
//
//        assertEquals(candidatoDTO.getNome(), candidatoEntity.getNome());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveTestarUpdateComException() throws RegraDeNegocioException, RegraDeNegocio404Exception {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        CandidatoCreateDTO candidatoCreateDTO = CandidatoFactory.getCandidatoCreateDTO();
//        candidatoCreateDTO.setEmail("");
//        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        CandidatoDTO candidatoDTO = candidatoService.update(1, candidatoCreateDTO);
//    }
//
//    @Test
//    public void deveTestarUpdateTecnicoComSucesso() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        CandidatoTecnicoNotaDTO candidatoTecnicoNotaDTO = new CandidatoTecnicoNotaDTO();
//        candidatoTecnicoNotaDTO.setNotaTecnico(80.0);
//        candidatoTecnicoNotaDTO.setParecerTecnico("observacao");
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        when(candidatoRepository.save(any(CandidatoEntity.class)))
//                .thenReturn(candidatoEntity);
//
//        CandidatoDTO candidatoRetorno = candidatoService.updateTecnico(1, candidatoTecnicoNotaDTO);
//
//        assertNotNull(candidatoRetorno);
//    }
//
//    @Test
//    public void deveTestarCalcularMediaNotas() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        candidatoEntity.setNotaProva(80.0);
//        candidatoEntity.setNotaEntrevistaTecnica(90.0);
//        candidatoEntity.setNotaEntrevistaComportamental(70.0);
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        when(candidatoRepository.save(any(CandidatoEntity.class)))
//                .thenReturn(candidatoEntity);
//
//        CandidatoDTO candidatoDTO = candidatoService.calcularMediaNotas(1);
//
//        assertNotNull(candidatoDTO);
//    }
//
//
//    @Test
//    public void deveTestarUpdateNota() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        CandidatoNotaDTO candidatoNotaDTO = new CandidatoNotaDTO();
//        candidatoNotaDTO.setNotaProva(80.0);
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        when(candidatoRepository.save(any(CandidatoEntity.class)))
//                .thenReturn(candidatoEntity);
//
//        CandidatoDTO candidatoRetorno = candidatoService.updateNota(1, candidatoNotaDTO);
//
//        assertNotNull(candidatoRetorno);
//    }
//
//    @Test
//    public void deveTestarUpdateComportamental() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        CandidatoNotaComportamentalDTO candidatoNotaComportamentalDTO = new CandidatoNotaComportamentalDTO();
//        candidatoNotaComportamentalDTO.setParecerComportamental("observacoes");
//        candidatoNotaComportamentalDTO.setNotaComportamental(80.0);
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        when(candidatoRepository.save(any(CandidatoEntity.class)))
//                .thenReturn(candidatoEntity);
//
//        CandidatoDTO candidatoDTO = candidatoService.updateComportamental(1, candidatoNotaComportamentalDTO);
//
//        assertNotNull(candidatoDTO);
//    }
//
//    @Test
//    public void deveTestarFindDtoByIdComSucesso() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//
//        when(candidatoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        CandidatoDTO candidatoDTO = candidatoService.findDtoById(1);
//
//        assertEquals(candidatoDTO.getNome(), candidatoEntity.getNome());
//    }
//
//    @Test
//    public void deveTestarFindByEmailComSucesso() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//
//        when(candidatoRepository.findByEmail(anyString()))
//                .thenReturn(Optional.of(candidatoEntity));
//
//        CandidatoDTO candidatoDTO = candidatoService.findByEmail("email@email");
//
//        assertEquals(candidatoDTO.getNome(), candidatoEntity.getNome());
//    }
//
//    @Test
//    public void deveTestarFindCandidatoDtoByEmailComSucesso() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//
//        when(candidatoRepository.findCandidatoEntitiesByEmail(anyString()))
//                .thenReturn(candidatoEntity);
//
//        CandidatoDTO candidatoDTO = candidatoService.findCandidatoDtoByEmail("email@email");
//
//        assertEquals(candidatoDTO.getNome(), candidatoEntity.getNome());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveTestarFindByEmailEntityComException() throws RegraDeNegocioException {
//
//        when(candidatoRepository.findByEmail(anyString()))
//                .thenReturn(Optional.empty());
//
//        candidatoService.findByEmailEntity("email@email");
//    }
//
//    @Test
//    public void deveTestarConvertToEntityComSucesso() throws RegraDeNegocio404Exception, RegraDeNegocioException {
//
//        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
//        candidatoDTO.setFormulario(FormularioFactory.getFormularioDto());
//
//        when(formularioService.findById(anyInt()))
//                .thenReturn(FormularioFactory.getFormularioEntity());
//
//        CandidatoEntity candidatoRetorno = candidatoService.convertToEntity(candidatoDTO);
//
//        assertEquals(candidatoDTO.getNome(), candidatoRetorno.getNome());
//    }
//
//    @Test
//    public void deveTestarListCandidatosByTrilhaComSucesso() throws RegraDeNegocioException {
//        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
//        formulario.setTrilhaEntitySet(new HashSet<>(List.of(trilhaEntity)));
//        candidatoEntity.setFormularioEntity(formulario);
//        List<CandidatoEntity> listCandidato = new ArrayList<>();
//        listCandidato.add(candidatoEntity);
//
//        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
//        List<CandidatoDTO> candidatoDTOList = new ArrayList<>();
//        candidatoDTOList.add(candidatoDTO);
//
//        when(trilhaService.findByNome(anyString()))
//                .thenReturn(trilhaEntity);
//
//        when(candidatoRepository.findCandidatoEntitiesByFormularioEntity_TrilhaEntitySet(any(TrilhaEntity.class)))
//                .thenReturn(listCandidato);
//
//        List<CandidatoDTO> listaRetorno = candidatoService.listCandidatosByTrilha("frontend");
//
//        assertNotNull(listaRetorno);
//
//    }
//
//    @Test
//    public void deveTestarListCandidatosByEdicao() throws RegraDeNegocioException {
//
//        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();
//        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
//        formulario.setTrilhaEntitySet(new HashSet<>(List.of(trilhaEntity)));
//        candidatoEntity.setFormularioEntity(formulario);
//        List<CandidatoEntity> listCandidato = new ArrayList<>();
//        listCandidato.add(candidatoEntity);
//
//        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
//        List<CandidatoDTO> candidatoDTOList = new ArrayList<>();
//        candidatoDTOList.add(candidatoDTO);
//
//        when(edicaoService.findByNome(anyString()))
//                .thenReturn(edicaoEntity);
//
//        when(candidatoRepository.findCandidatoEntitiesByEdicao(any(EdicaoEntity.class)))
//                .thenReturn(listCandidato);
//
//        List<CandidatoDTO> listaRetorno = candidatoService.listCandidatosByEdicao("VemSer12");
//
//        assertNotNull(listaRetorno);
//    }
//
//    @Test
//    public void deveTestarExportarCandidatoCSVComSucesso() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        List<CandidatoEntity> candidatoEntityList = new ArrayList<>();
//        candidatoEntityList.add(candidatoEntity);
//
//        BufferedWriter writer = mock(BufferedWriter.class);
//        OutputStreamWriter streamWriter = mock(OutputStreamWriter.class);
//        FileOutputStream fileOutputStream=mock(FileOutputStream.class);
//
//        when(candidatoRepository.findAll())
//                .thenReturn(candidatoEntityList);
//
//        candidatoService.exportarCandidatoCSV();
//    }
//
//    @Test
//    public void deveTestarListCandidatosByNotaComSucesso() {
//
//        Sort orderBy = Sort.by("notaProva");
//        PageRequest pageRequest = PageRequest.of(0, 1, orderBy);
//        Page<CandidatoEntity> page = mock(Page.class);
//
//        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
//        List<CandidatoDTO> candidatoDTOList = new ArrayList<>();
//        candidatoDTOList.add(candidatoDTO);
//
//        when(candidatoRepository.findByNota(any(PageRequest.class)))
//                .thenReturn(page);
//
//        PageDTO<CandidatoDTO> listCandidatoPage = candidatoService.listCandidatosByNota(0,1);
//
//        assertNotNull(listCandidatoPage);
//    }
//}
>>>>>>> 273d9a483054ab770e02bd7890203c9ada3ccd58
