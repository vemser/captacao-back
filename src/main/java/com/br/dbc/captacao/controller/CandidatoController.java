package com.br.dbc.captacao.controller;


import com.br.dbc.captacao.controller.documentationinterface.CandidatoControllerInterface;
import com.br.dbc.captacao.dto.candidato.*;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.CandidatoService;
import com.br.dbc.captacao.service.ImagemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/candidato")
public class CandidatoController implements CandidatoControllerInterface {
    private final CandidatoService candidatoService;
    private final ImagemService imagemService;

    @GetMapping
    public PageDTO<CandidatoDTO> list(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                      @RequestParam(defaultValue = "20" ,required = false) Integer tamanho,
                                      @RequestParam(defaultValue = "idCandidato" , required = false) String sort,
                                      @RequestParam(defaultValue = "0",required = false ) Integer order) throws RegraDeNegocioException {
        return candidatoService.listaAllPaginado(pagina,tamanho,sort,order);
    }

    @GetMapping("/findbyemails")
    public CandidatoDTO findByEmail(@RequestParam ("email") String email) throws RegraDeNegocioException {
        return candidatoService.findByEmail(email);
    }

    @PostMapping
    public ResponseEntity<CandidatoDTO> create(@Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException, IOException, RegraDeNegocio404Exception {
        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{idCandidato}")
    public ResponseEntity<CandidatoDTO> update(@PathVariable("idCandidato") Integer id,
                                               @Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        CandidatoDTO candidatoDTO = candidatoService.update(id, candidatoCreateDTO);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }
//
//    @GetMapping("/listar-candidato-cadastro-por-nome-ou-por-trilha")
//    public PageDTO<RelatorioCandidatoCadastroDTO> listRelatorioCandidatoCadastroDTO(@RequestParam(value = "nomeCompleto", required = false) String nomeCompleto,
//                                                                                    @RequestParam(defaultValue = "0") Integer pagina,
//                                                                                    @RequestParam(defaultValue = "20") Integer tamanho,
//                                                                                    @RequestParam(required = false) String nomeTrilha,
//                                                                                    @RequestParam(required = false) String nomeEdicao) throws RegraDeNegocioException {
//        return candidatoService.listRelatorioCandidatoCadastroDTO(nomeCompleto, pagina, tamanho, nomeTrilha, nomeEdicao);
//    }
//
//    @GetMapping("/listar-candidato-principal-por-nome-ou-por-trilha")
//    public PageDTO<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(@RequestParam(value = "nomeCompleto", required = false) String nomeCompleto,
//                                                                                                           @RequestParam(defaultValue = "0") Integer pagina,
//                                                                                                           @RequestParam(defaultValue = "20") Integer tamanho,
//                                                                                                           @RequestParam(required = false) String nomeTrilha,
//                                                                                                           @RequestParam(required = false) String nomeEdicao) throws RegraDeNegocioException {
//        return candidatoService.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(nomeCompleto, pagina, tamanho, nomeTrilha, nomeEdicao);
//    }

    @DeleteMapping("/{idCandidato}")
    public ResponseEntity<CandidatoDTO> deleteLogico(@PathVariable("idCandidato") Integer id) throws RegraDeNegocioException {
        candidatoService.deleteLogicoById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/upload-foto/{email}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadFoto(@RequestPart("file") MultipartFile file,
                                           @PathVariable("email") String email) throws RegraDeNegocioException, IOException {
        imagemService.arquivarCandidato(file, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recuperar-imagem")
    public ResponseEntity<String> recuperarImagem(@RequestParam("email") String email) throws RegraDeNegocioException {
        return new ResponseEntity<>(imagemService.pegarImagemCandidato(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-fisico/{idCandidato}")
    public ResponseEntity<CandidatoDTO> deleteFisico(@PathVariable("idCandidato") Integer id) throws RegraDeNegocioException {
        candidatoService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/nota-prova/{idCandidato}")
    public ResponseEntity<CandidatoDTO> updateNota(@PathVariable("idCandidato") Integer id,
                                                   @Valid @RequestBody CandidatoNotaDTO candidatoNotaDTO) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.updateNota(id, candidatoNotaDTO);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }
    @PutMapping("/nota-comportamental/{idCandidato}")
    public ResponseEntity<CandidatoDTO> updateComportamental(@PathVariable("idCandidato") Integer id,
                                                             @Valid @RequestBody CandidatoNotaComportamentalDTO candidatoNotaComportamentalDTO) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.updateComportamental(id, candidatoNotaComportamentalDTO);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }

    @PutMapping("/nota-parecer-tecnico/{idCandidato}")
    public ResponseEntity<CandidatoDTO> updateNotaEParecerTecnico(@PathVariable("idCandidato") Integer id,
                                                   @Valid @RequestBody CandidatoTecnicoNotaDTO candidatoNotaDTO) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.updateTecnico(id, candidatoNotaDTO);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }

    @GetMapping("/find-by-trilha")
    public ResponseEntity<List<CandidatoDTO>> findCandidatosByTrilha (@RequestParam("trilha") String trilha) throws RegraDeNegocioException {

        List<CandidatoDTO> candidatoDTOListByTrilha = candidatoService.listCandidatosByTrilha(trilha);

        return new ResponseEntity<>(candidatoDTOListByTrilha,HttpStatus.OK);
    }

    @GetMapping("/find-by-edicao")
    public ResponseEntity<List<CandidatoDTO>> findCandidatosByEdicao ( @RequestParam("edicao") String edicao) throws  RegraDeNegocioException{

        List<CandidatoDTO> candidatoDTOListByEdicao = candidatoService.listCandidatosByEdicao(edicao);

        return new ResponseEntity<>(candidatoDTOListByEdicao,HttpStatus.OK);
    }

    @GetMapping("/find-by-nota")
    public ResponseEntity<PageDTO<CandidatoDTO>> findCandidatosByNota (Integer pagina, Integer tamanho) {

        PageDTO<CandidatoDTO> candidatoDTOListByNota = candidatoService.listCandidatosByNota(pagina, tamanho);

        return new ResponseEntity<>(candidatoDTOListByNota,HttpStatus.OK);
    }
}