package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.TrilhaControllerInterface;
import com.br.dbc.captacao.dto.trilha.TrilhaCreateDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.TrilhaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/trilha")
public class TrilhaController implements TrilhaControllerInterface {

    private final TrilhaService trilhaService;

    @PostMapping
    public ResponseEntity<TrilhaDTO> create(@RequestBody TrilhaCreateDTO trilhaCreateDTO) throws RegraDeNegocioException {
        TrilhaDTO trilhaDTO = trilhaService.create(trilhaCreateDTO);
        return new ResponseEntity<>(trilhaDTO, HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TrilhaDTO>> listarTodos(){
        return new ResponseEntity<>(trilhaService.list(), HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{idTrilha}")
    public ResponseEntity<Void>delete(@PathVariable("idTrilha") Integer idTrilha) throws RegraDeNegocioException{
        trilhaService.deleteFisico(idTrilha);
        return ResponseEntity.noContent().build();
    }
}
