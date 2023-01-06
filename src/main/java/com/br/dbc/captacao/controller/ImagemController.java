package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.ImagemControllerInterface;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.ImagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/imagem")
public class ImagemController implements ImagemControllerInterface {

    private final ImagemService imagemService;

    @DeleteMapping("/delete-fisico/{idImagem}")
    public ResponseEntity<Void> deleteFisico(@PathVariable("idImagem") Integer id) throws RegraDeNegocioException {
        imagemService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }
}
