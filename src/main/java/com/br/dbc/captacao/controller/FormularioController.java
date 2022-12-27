package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.FormularioControllerInterface;
import com.br.dbc.captacao.dto.formulario.FormularioCreateDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.CurriculoService;
import com.br.dbc.captacao.service.FormularioService;
import com.br.dbc.captacao.service.PrintConfigPCService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/formulario")
@Validated
@RequiredArgsConstructor
public class FormularioController implements FormularioControllerInterface {
    private final FormularioService formularioService;
    private final CurriculoService curriculoService;
    private final PrintConfigPCService printConfigPCService;

    @PostMapping("/cadastro")
    public ResponseEntity<FormularioDTO> create(@RequestBody @Valid FormularioCreateDTO formularioCreateDto) throws RegraDeNegocioException {
        log.info("Criando formulario...");
        FormularioDTO formularioDto = formularioService.create(formularioCreateDto);
        log.info("Formulario criado, id:" + formularioDto.getIdFormulario());
        return new ResponseEntity<>(formularioDto, HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<PageDTO<FormularioDTO>> listAll(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                          @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                          @RequestParam(defaultValue = "idFormulario", required = false) String sort,
                                                          @RequestParam(defaultValue = "0", required = false) int order) throws RegraDeNegocioException {
        log.info("Listando todos os formulários");
        return new ResponseEntity<>(formularioService.listAllPaginado(pagina, tamanho, sort, order), HttpStatus.OK);
    }

    @PutMapping("/atualizar-formulario/{idFormulario}")
    public ResponseEntity<FormularioDTO> updateFormulario(@PathVariable("idFormulario") Integer idFormulario,
                                                          @RequestBody @Valid FormularioCreateDTO formularioCreateDto) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        FormularioDTO formularioDto = formularioService.update(idFormulario, formularioCreateDto);
        log.info("Atualizando Formulario ID: " + idFormulario);
        return new ResponseEntity<>(formularioDto, HttpStatus.OK);
    }

    @PutMapping(value = "/upload-curriculo/{idFormulario}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadCurriculo(@RequestPart("file") MultipartFile file,
                                                @PathVariable("idFormulario") Integer idFormulario) throws RegraDeNegocioException, IOException, RegraDeNegocio404Exception {
        curriculoService.arquivarCurriculo(file, idFormulario);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/upload-print-config-pc/{idFormulario}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadPrintConfigPc(@RequestPart("file") MultipartFile file,
                                                    @PathVariable("idFormulario") Integer idFormulario) throws RegraDeNegocioException, IOException, RegraDeNegocio404Exception {
        printConfigPCService.arquivarPrintConfigPc(file, idFormulario);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recuperar-curriculo")
    public ResponseEntity<String> recuperarCurriculo(@RequestParam("idFormulario") Integer idFormulario) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        return new ResponseEntity<>(curriculoService.pegarCurriculoCandidato(idFormulario), HttpStatus.OK);
    }

    @DeleteMapping("/delete-fisico/{idFormulario}")
    public ResponseEntity<Void> deletarFormulario(@PathVariable("idFormulario") Integer idFormulario) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        formularioService.deleteById(idFormulario);
        return ResponseEntity.noContent().build();
    }
}
