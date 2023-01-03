package com.br.dbc.captacao.service;


import com.br.dbc.captacao.dto.formulario.FormularioCreateDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CurriculoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.PrintConfigPCEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.FormularioFactory;
import com.br.dbc.captacao.factory.TrilhaFactory;
import com.br.dbc.captacao.repository.FormularioRepository;
import com.br.dbc.captacao.repository.PrintConfigPCRepository;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FormularioServiceTest {

    @InjectMocks
    private FormularioService formularioService;

    @Mock
    private FormularioRepository formularioRepository;

    @Mock
    private PrintConfigPCRepository printConfigPCRepository;

    @Mock
    private TrilhaService trilhaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(formularioService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCreateFormularioComSucesso() throws RegraDeNegocioException {
        FormularioCreateDTO formularioCreateDto = FormularioFactory.getFormularioCreateDto();

        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();

        Set<TrilhaEntity> listTrilha = new HashSet<>();
        listTrilha.add(trilha);

        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
        when(trilhaService.findListaTrilhas(anyList())).thenReturn(listTrilha);
        when(formularioRepository.save(any())).thenReturn(FormularioFactory.getFormularioEntity());

        FormularioDTO formularioDtoRetorno = formularioService.create(formularioCreateDto);

        assertNotNull(formularioDtoRetorno);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateFormularioComException() throws RegraDeNegocioException {

        FormularioCreateDTO formularioCreateDto = FormularioFactory.getFormularioCreateDto();
        formularioCreateDto.setMatriculadoBoolean(false);

        formularioService.create(formularioCreateDto);

        verify(formularioRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        Integer id = 10;
        FormularioCreateDTO formularioCreateDto = FormularioFactory.getFormularioCreateDto();

        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();
        formularioEntity.setIdFormulario(1);
        when(formularioRepository.findById(anyInt())).thenReturn(Optional.of(formularioEntity));

        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        when(formularioRepository.save(any())).thenReturn(formulario);


        FormularioDTO formularioDto = formularioService.update(id, formularioCreateDto);

        assertNotNull(formularioDto);
        assertNotEquals("github.com/vemser/vemvemser-back", formularioDto.getGithub());
    }

    @Test
    public void deveTestarUpdateCurriculoComSucesso() throws IOException, RegraDeNegocioException {

        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();

        byte[] imagemBytes = new byte[10 * 1024];

        MultipartFile imagem = new MockMultipartFile("curriculo.pdf", "curriculo.pdf", ".pdf", imagemBytes);

        CurriculoEntity curriculo = new CurriculoEntity();
        curriculo.setIdCurriculo(1);
        curriculo.setTipo(imagem.getContentType());
        curriculo.setNome(imagem.getName());
        curriculo.setData(imagem.getBytes());


        formularioService.updateCurriculo(formularioEntity, curriculo);

        verify(formularioRepository, times(1)).save(any(FormularioEntity.class));
    }

    @Test
    public void deveTestarFindDtoByIdComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();

        when(formularioRepository.findById(anyInt())).thenReturn(Optional.of(formularioEntity));

        FormularioDTO formularioRetorno = formularioService.findDtoById(1);

        assertEquals(formularioRetorno.getIdFormulario(), formularioEntity.getIdFormulario());
    }

    @Test
    public void deveTestarDeleteByIdComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();

        when(formularioRepository.findById(anyInt())).thenReturn(Optional.of(formularioEntity));

        formularioService.deleteById(10);

        verify(formularioRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocio404Exception {
        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();

        when(formularioRepository.findById(anyInt())).thenReturn(Optional.of(formularioEntity));

        FormularioEntity formularioRetorno = formularioService.findById(1);

        assertEquals(formularioEntity, formularioRetorno);
    }

    @Test(expected = RegraDeNegocio404Exception.class)
    public void deveTestarFindByIdComRegraDeNegocio404Exception() throws RegraDeNegocio404Exception {
        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();

        FormularioEntity formularioRetorno = formularioService.findById(1);
    }

    @Test
    public void deveTestarConvertToEntityComSucesso() {
        FormularioDTO formularioDTO = FormularioFactory.getFormularioDto();

        when(trilhaService.convertToEntity(any())).thenReturn(FormularioFactory.getFormularioEntity().getTrilhaEntitySet());

        FormularioEntity formularioRetorno = formularioService.convertToEntity(formularioDTO);

        assertEquals(formularioRetorno.getIdFormulario(), formularioDTO.getIdFormulario());
    }

    @Test
    public void deveTestarListAllPaginadoComSucesso() throws RegraDeNegocioException {

        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idFormulario";
        int orderDescendente = 1;

        Sort ordenacao = Sort.by(sort).descending();

        PageImpl<FormularioEntity> pageImpl = new PageImpl<>(List.of(FormularioFactory.getFormularioEntity()),
                PageRequest.of(pagina, tamanho, ordenacao), 0);
        pageImpl.stream()
                .map(formularioEntity -> {
                    CurriculoEntity curriculoEntity = new CurriculoEntity();
                    PrintConfigPCEntity printConfigPCEntity = new PrintConfigPCEntity();
                    formularioEntity.setCurriculoEntity(curriculoEntity);
                    formularioEntity.setImagemConfigPc(printConfigPCEntity);
                    return formularioEntity;
                }).toList();

        when(formularioRepository.listarFormulariosSemVazios(any(Pageable.class))).thenReturn(pageImpl);

        PageDTO<FormularioDTO> page = formularioService.listAllPaginado(pagina, tamanho, sort, orderDescendente);

        assertEquals(page.getTamanho(), 5);
    }

    @Test
    public void deveTestarListAllPaginadoRetornandoListaVaziaComSucesso() throws RegraDeNegocioException {

        Integer pagina = 1;
        Integer tamanho = 1;
        String sort = "idFormulario";
        int orderDescendente = 0;

        Sort ordenacao = Sort.by(sort).descending();

        PageImpl<FormularioEntity> pageImpl = new PageImpl<>(List.of(FormularioFactory.getFormularioEntity()),
                PageRequest.of(pagina, tamanho, ordenacao), 0);
        pageImpl.stream()
                .map(formularioEntity -> {
                    CurriculoEntity curriculoEntity = new CurriculoEntity();
                    PrintConfigPCEntity printConfigPCEntity = new PrintConfigPCEntity();
                    formularioEntity.setCurriculoEntity(curriculoEntity);
                    formularioEntity.setImagemConfigPc(printConfigPCEntity);
                    return formularioEntity;
                }).toList();

        PageDTO<FormularioDTO> page = formularioService.listAllPaginado(pagina, 0, sort, orderDescendente);

        assertEquals(page.getTamanho(), 0);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarListAllPaginadoComException() throws RegraDeNegocioException {

        Integer pagina = -1;
        Integer tamanho = -1;
        String sort = "idFormulario";
        int orderDescendente = 1;

        PageDTO<FormularioDTO> page = formularioService.listAllPaginado(pagina, tamanho, sort, orderDescendente);
    }
}
