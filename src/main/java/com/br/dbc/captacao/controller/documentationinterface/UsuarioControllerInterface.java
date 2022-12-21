package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.gestor.GestorCreateDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.gestor.GestorEmailNomeCargoDTO;
import com.br.dbc.captacao.dto.gestor.GestorSenhaDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UsuarioControllerInterface {

    @Operation(summary = "Listar Gestores", description = "Lista todos os gestores do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de gestores"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<GestorDTO>> listar(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                     @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                     @RequestParam(defaultValue = "idGestor", required = false) String sort,
                                                     @RequestParam(defaultValue = "0", required = false) int order);

    @Operation(summary = "Listar Gestores por ID", description = "Retorna um Gestor por ID do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna Gestor"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/id-gestor")
    ResponseEntity<GestorDTO> findById(@RequestParam Integer idGestor) throws RegraDeNegocioException;

//    @Operation(summary = "Cadastrar um novo colaborador/administrador", description = "Cadastro de colaborador/administrador")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso"),
//                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
//                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
//            }
//    )
//    @PostMapping("/cadastro")
//    public ResponseEntity<GestorDTO> cadastroCandidato(@Valid @RequestBody GestorCreateDTO gestorCreateDTO) throws RegraDeNegocioException;

//    @Operation(summary = "Atualizar o colaborador/administrador", description = "Atualiza o colaborador/administrador no banco de dados")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "Atualizou com sucesso"),
//                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
//                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
//            }
//    )
//    @PutMapping("/{idGestor}")
//    ResponseEntity<GestorDTO> editar(@PathVariable(name = "idGestor") Integer idGestor,
//                                            @Valid @RequestBody GestorCreateDTO gestor) throws RegraDeNegocioException

//    @Operation(summary = "Atualizar a senha do colaborador/administrador", description = "Atualiza a senha do colaborador/administrador no banco de dados")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "Atualizou com sucesso"),
//                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
//                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
//            }
//    )
//    @PutMapping("/trocar-senha/{idGestor}")
//    ResponseEntity<GestorDTO> editarSenhaGestor(@PathVariable(name = "idGestor") Integer idGestor,
//                                                       @Valid @RequestBody GestorSenhaDTO gestor) throws RegraDeNegocioException;

    @Operation(summary = "Deletar colaborador/administrador", description = "Deletar o colaborador/administrador no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "404", description = "Não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idGestor}")
    ResponseEntity<Void> remover(@PathVariable(name = "idGestor") Integer idGestor) throws RegraDeNegocioException;

    @Operation(summary = "Desativar conta", description = "Desativar sua conta do gestor")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Conta desativada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/desativacao-conta/{idGestor}")
    ResponseEntity<GestorDTO> desativar(@PathVariable(name = "idGestor") Integer idGestor) throws RegraDeNegocioException;

    @Operation(summary = "Listar contas inativas", description = "Listar todas as contas inativas")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de gestores inativos"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/contas-inativas")
    ResponseEntity<List<GestorDTO>> listarContaInativas();

    @Operation(summary = "Pegar gestor pelo email,nome e cargo", description = "Pegar o gestor pelo nome,email e cargo informado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Gestor pego com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/gestor-by-nome-email")
    ResponseEntity<List<GestorDTO>> pegarGestorPorEmailNomeCargo(@RequestBody GestorEmailNomeCargoDTO gestorEmailNomeCargoDto) throws RegraDeNegocioException;

//    @Operation(summary = "Pegar conta logada", description = "Pegar sua conta logado no sistema")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "Gestor pego com sucesso"),
//                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
//                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
//            }
//    )
//    @GetMapping("/gestor-logado")
//    public ResponseEntity<GestorDTO> pegarUserLogado() throws RegraDeNegocioException;
}
