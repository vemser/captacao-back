package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.UsuarioControllerInterface;
import com.br.dbc.captacao.dto.gestor.GestorCreateDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.gestor.GestorEmailNomeCargoDTO;
import com.br.dbc.captacao.dto.gestor.GestorSenhaDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.GestorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/usuario")
@Validated
@RequiredArgsConstructor
public class UsuarioController implements UsuarioControllerInterface {

    private final GestorService gestorService;

    public ResponseEntity<PageDTO<GestorDTO>> listar(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                     @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                     @RequestParam(defaultValue = "idGestor", required = false) String sort,
                                                     @RequestParam(defaultValue = "0", required = false) int order) {
        log.info("Listando gestores...");
        return new ResponseEntity<>(gestorService.listar(pagina, tamanho, sort, order), HttpStatus.OK);
    }

    @GetMapping("/id-gestor")
    public ResponseEntity<GestorDTO> findById(@RequestParam Integer idGestor) throws RegraDeNegocioException {
        log.info("Buscando gestor com id:" + idGestor + "...");
        GestorDTO usuarioDTO = gestorService.findDtoById(idGestor);
        log.info("Gestor encontrado.");
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

//    @PostMapping("/cadastro")
//    public ResponseEntity<GestorDTO> cadastroCandidato(@Valid @RequestBody GestorCreateDTO gestorCreateDTO) throws RegraDeNegocioException {
//        log.info("Cadastrando novo gestor...");
//        GestorDTO gestorDto = gestorService.cadastrar(gestorCreateDTO);
//        log.info("Gestor cadastrado.");
//        return ResponseEntity.ok(gestorDto);
//    }

//    @PutMapping("/{idGestor}")
//    public ResponseEntity<GestorDTO> editar(@PathVariable(name = "idGestor") Integer idGestor,
//                                            @Valid @RequestBody GestorCreateDTO gestor) throws RegraDeNegocioException {
//        log.info("Editando o Gestor...");
//        GestorDTO gestorEditado = gestorService.editar(idGestor, gestor);
//        log.info("Gestor editado com sucesso!");
//        return new ResponseEntity<>(gestorEditado, HttpStatus.OK);
//    }

//    @PutMapping("/trocar-senha/{idGestor}")
//    public ResponseEntity<GestorDTO> editarSenhaGestor(@PathVariable(name = "idGestor") Integer idGestor,
//                                                       @Valid @RequestBody GestorSenhaDTO gestor) throws RegraDeNegocioException {
//        log.info("Editando senha do Gestor...");
//        GestorDTO gestorEditado = gestorService.editarSenha(idGestor, gestor);
//        log.info("Gestor senha editado com sucesso!");
//        return new ResponseEntity<>(gestorEditado, HttpStatus.OK);
//    }

    @DeleteMapping("/{idGestor}")
    public ResponseEntity<Void> remover(@PathVariable(name = "idGestor") Integer idGestor) throws RegraDeNegocioException {
        log.info("Deletando gestor...");
        gestorService.remover(idGestor);
        log.info("Gestor deletado com sucesso");
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/desativacao-conta/{idGestor}")
    public ResponseEntity<GestorDTO> desativar(@PathVariable(name = "idGestor") Integer idGestor) throws RegraDeNegocioException {
        log.info("Desativando gestor com id:" + idGestor);
        return new ResponseEntity<>(gestorService.desativarConta(idGestor), HttpStatus.OK);

    }

    @GetMapping("/contas-inativas")
    public ResponseEntity<List<GestorDTO>> listarContaInativas() {
        log.info("Listando contas inativas");
        return new ResponseEntity<>(gestorService.contasInativas(), HttpStatus.OK);
    }

    @PostMapping("/gestor-by-nome-email")
    public ResponseEntity<List<GestorDTO>> pegarGestorPorEmailNomeCargo(@RequestBody GestorEmailNomeCargoDTO gestorEmailNomeCargoDto) throws RegraDeNegocioException {
        log.info("Buscando gestor por cargo e ( email ou nome )");
        return new ResponseEntity<>(gestorService.findGestorbyNomeOrEmail(gestorEmailNomeCargoDto), HttpStatus.OK);
    }

//    @GetMapping("/gestor-logado")
//    public ResponseEntity<GestorDTO> pegarUserLogado() throws RegraDeNegocioException {
//        log.info("Buscando gestor logado.");
//        return new ResponseEntity<>(gestorService.getLoggedUser(), HttpStatus.OK);
//    }

}
