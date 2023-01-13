package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.PrintConfigPCEntity;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.FormularioFactory;
import com.br.dbc.captacao.factory.PrintConfigPCFactory;
import com.br.dbc.captacao.repository.PrintConfigPCRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PrintConfigPCServiceTest {

    @Mock
    private PrintConfigPCRepository printConfigPCRepository;

    @InjectMocks
    private PrintConfigPCService printConfigPCService;

    @Mock
    private FormularioService formularioService;

    @Test
    public void testarDeletarFisicamenteComSucesso() throws RegraDeNegocioException{
        final Integer id = 1;
        when(printConfigPCRepository.findById(anyInt())).thenReturn(Optional.of(PrintConfigPCFactory.getPrintEntity()));
        printConfigPCService.deleteFisico(id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarDeletarFisicamenteComErro() throws RegraDeNegocioException{
        final Integer id = 2;

        printConfigPCService.deleteFisico(id);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException{
        Integer id = 1;

        when(printConfigPCRepository.findById(anyInt())).thenReturn(Optional.of(PrintConfigPCFactory.getPrintEntity()));
        PrintConfigPCEntity printConfigPCEntity = printConfigPCService.findById(id);

        assertNotNull(printConfigPCEntity);
        assertEquals(printConfigPCEntity.getIdImagem(), id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarFindByIdComErro() throws RegraDeNegocioException{
        Integer id = 2;

        when(printConfigPCRepository.findById(anyInt())).thenReturn(Optional.empty());

        printConfigPCService.findById(id);
    }


    @Test
    public void testarUploadCasoNaoExistaPrintComSucesso() throws RegraDeNegocioException, IOException, RegraDeNegocio404Exception{

        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        byte[] printBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile print = new MockMultipartFile("print.png", "print.png", ".png", printBytes);

        when(formularioService.findById(any())).thenReturn(formulario);
        when(printConfigPCRepository.save(any())).thenReturn(PrintConfigPCFactory.getPrintEntity());

        printConfigPCService.arquivarPrintConfigPc(print, formulario.getIdFormulario());

        verify(printConfigPCRepository, times(1)).save(any());
        verify(formularioService, times(1)).save(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUploadComException() throws RegraDeNegocioException, IOException, RegraDeNegocio404Exception{
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        byte[] printBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile print = new MockMultipartFile("print.abc", "print.abc", ".abc", printBytes);

        printConfigPCService.arquivarPrintConfigPc(print, formulario.getIdFormulario());
    }

//    @Test
//    public void deveProcurarFormularioComSucesso() {
//        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
//        final Integer id = 1;
//
//        when(printConfigPCRepository.findByFormulario(any())).thenReturn(Optional.of(PrintConfigPCFactory.getPrintEntity()));
//
//        Optional<PrintConfigPCEntity> printConfigPCEntity = printConfigPCService.findByFormulario(formulario);
//
//        assertEquals(printConfigPCEntity.get().getFormulario(), formulario);
//    }
}
