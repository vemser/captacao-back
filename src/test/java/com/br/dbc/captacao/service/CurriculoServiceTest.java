package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.CurriculoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CurriculoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HexFormat;
import java.util.Optional;

import static com.br.dbc.captacao.factory.CurriculoFactory.getCurriculoEntity;
import static com.br.dbc.captacao.factory.FormularioFactory.getFormularioEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CurriculoServiceTest {
    @InjectMocks
    private CurriculoService curriculoService;

    @Mock
    private FormularioService formularioService;

    @Mock
    private CurriculoRepository curriculoRepository;

    @Test
    public void deveTestarArquivarCurriculoComSucesso() throws RegraDeNegocioException, IOException, RegraDeNegocio404Exception {
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile curriculo = new MockMultipartFile("curriculo", "curriculo.pdf", "pdf", bytes);
        FormularioEntity formularioEntity = getFormularioEntity();

        when(formularioService.findById(any())).thenReturn(formularioEntity);
        when(curriculoRepository.save(any())).thenReturn(getCurriculoEntity());

        curriculoService.arquivarCurriculo(curriculo, 1);

        verify(curriculoRepository, times(1)).save(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoFormatoDoArquivoForInvalido() throws RegraDeNegocio404Exception, RegraDeNegocioException, IOException {
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile curriculo = new MockMultipartFile("curriculo", "curriculo.doc", "pdf", bytes);
        FormularioEntity formularioEntity = getFormularioEntity();

        when(formularioService.findById(any())).thenReturn(formularioEntity);

        curriculoService.arquivarCurriculo(curriculo, 1);
    }

    @Test
    public void devePegarCurriculoCandidatoComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        CurriculoEntity curriculoEntity = getCurriculoEntity();
        FormularioEntity formularioEntity = getFormularioEntity();
        formularioEntity.setCurriculoEntity(curriculoEntity);

        when(formularioService.findById(any())).thenReturn(formularioEntity);
        String curriculoBase64 = curriculoService.pegarCurriculoCandidato(1);

        assertNotNull(curriculoBase64);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarCurriculoCandidatoComErro() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        FormularioEntity formularioEntity = getFormularioEntity();

        when(formularioService.findById(anyInt())).thenReturn(formularioEntity);
        curriculoService.pegarCurriculoCandidato(1);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        Integer id = 2;

        when(curriculoRepository.findById(anyInt())).thenReturn(Optional.of(getCurriculoEntity()));
        CurriculoEntity curriculo = curriculoService.findById(id);

        assertNotNull(curriculo);
        assertEquals(curriculo.getIdCurriculo(), id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByIdComErro() throws RegraDeNegocioException {
        Integer id = 1;
        when(curriculoRepository.findById(anyInt())).thenReturn(Optional.empty());
        curriculoService.findById(id);
    }

    @Test
    public void testarDeletarFisicamenteUsuarioComSucesso() throws RegraDeNegocioException {
        CurriculoEntity curriculoEntity = getCurriculoEntity();

        when(curriculoRepository.findById(any())).thenReturn(Optional.of(curriculoEntity));
        curriculoService.deleteFisico(1);

        verify(curriculoRepository).deleteById(any());
    }
}
