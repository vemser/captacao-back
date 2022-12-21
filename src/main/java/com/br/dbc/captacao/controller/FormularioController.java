package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.FormularioControllerInterface;
import com.br.dbc.captacao.dto.formulario.FormularioCreateDto;
import com.br.dbc.captacao.dto.formulario.FormularioDto;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.FormularioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/formulario")
@Validated
@RequiredArgsConstructor
public class FormularioController implements FormularioControllerInterface {
    private final FormularioService formularioService;

    @PostMapping("/cadastro")
    public ResponseEntity<FormularioDto> create(@RequestBody FormularioCreateDto formularioCreateDto) throws RegraDeNegocioException {
        log.info("Criando formulario...");
        FormularioDto formularioDto = formularioService.create(formularioCreateDto);
        log.info("Formulario criado, id:" + formularioDto.getIdFormulario());
        return new ResponseEntity<>(formularioDto, HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<PageDTO<FormularioDto>> listAll(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                          @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                          @RequestParam(defaultValue = "idFormulario", required = false) String sort,
                                                          @RequestParam(defaultValue = "0", required = false) int order) {
        log.info("Listando todos os formulários");
        return new ResponseEntity<>(formularioService.listAllPaginado(pagina, tamanho, sort, order), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<FormularioDto> updateFormulario(@RequestParam Integer idFormulario,
                                                          @RequestBody @Valid FormularioCreateDto formularioCreateDto) throws RegraDeNegocioException {
        FormularioDto formularioDto = formularioService.update(idFormulario, formularioCreateDto);
        log.info("Atualizando Formulario ID: " + idFormulario);
        return new ResponseEntity<>(formularioDto, HttpStatus.OK);
    }

    @DeleteMapping
    public void deletarFormulario(@RequestParam Integer idFormulario) throws RegraDeNegocioException {
        formularioService.deleteById(idFormulario);
        log.info("Deletando Formulario ID: " + idFormulario);
        new ResponseEntity<>(null, HttpStatus.OK);
    }
}
