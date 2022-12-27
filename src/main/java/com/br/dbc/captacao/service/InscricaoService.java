package com.br.dbc.captacao.service;


import com.br.dbc.captacao.dto.SendEmailDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoCreateDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.InscricaoEntity;
import com.br.dbc.captacao.enums.TipoEmail;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.InscricaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscricaoService {

    private static final int DESCENDING = 1;
    private final InscricaoRepository inscricaoRepository;
    private final CandidatoService candidatoService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public InscricaoDTO create(Integer idCandidato) throws RegraDeNegocioException {
//        if (!inscricaoRepository.findInscricaoEntitiesByCandidato_IdCandidato(inscricaoCreateDTO.getIdCandidato()).isEmpty()) {
//            throw new RegraDeNegocioException("Inscrição já realizada");
//        }

        InscricaoEntity inscricaoEntity = new InscricaoEntity();
        inscricaoEntity.setCandidato(candidatoService.findById(idCandidato));
        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(TipoMarcacao.F);
        InscricaoEntity inscricaoRetorno = inscricaoRepository.save(inscricaoEntity);
        InscricaoDTO inscricaoDto = converterParaDTO(inscricaoRetorno);
//        SendEmailDTO sendEmailDTO = new SendEmailDTO();
//        sendEmailDTO.setNome(inscricaoEntity.getCandidato().getNome());
//        sendEmailDTO.setEmail(inscricaoDto.getCandidato().getEmail());
//        emailService.sendEmail(sendEmailDTO, TipoEmail.INSCRICAO);

        return inscricaoDto;
    }

    public InscricaoEntity setAvaliado(Integer idInscricao) throws RegraDeNegocioException {
        InscricaoEntity inscricaoEntity = findById(idInscricao);
        inscricaoEntity.setAvaliado(TipoMarcacao.T);
        InscricaoEntity inscricao = inscricaoRepository.save(inscricaoEntity);
        return inscricao;
    }

    public List<InscricaoDTO> findInscricaoPorEmail(String email) {
        List<InscricaoEntity> lista = inscricaoRepository.findInscricaoEntitiesByCandidato_Email(email);
        return lista.stream().map(inscricaoEntity -> converterParaDTO(inscricaoEntity))
                .toList();
    }

    public PageDTO<InscricaoDTO> listar(Integer pagina, Integer tamanho, String sort, int order) {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<InscricaoEntity> paginaInscricaoEntities = inscricaoRepository.findAll(pageRequest);

        List<InscricaoDTO> inscricaoDtos = paginaInscricaoEntities.getContent().stream()
                .map(inscricaoEntity -> converterParaDTO(inscricaoEntity)).toList();

        return new PageDTO<>(paginaInscricaoEntities.getTotalElements(),
                paginaInscricaoEntities.getTotalPages(),
                pagina,
                tamanho,
                inscricaoDtos);
    }

    public InscricaoDTO findDtoByid(Integer idInscricao) throws RegraDeNegocioException {
        InscricaoDTO inscricaoDto = converterParaDTO(findById(idInscricao));
        return inscricaoDto;
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        findById(id);
        inscricaoRepository.deleteById(id);
    }


    public InscricaoEntity findById(Integer idInscricao) throws RegraDeNegocioException {
        return inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new RegraDeNegocioException("ID_Inscrição inválido"));

    }

    public InscricaoDTO converterParaDTO(InscricaoEntity inscricaoEntity) {
        InscricaoDTO inscricaoDto = objectMapper.convertValue(inscricaoEntity, InscricaoDTO.class);
        inscricaoDto.setCandidato(candidatoService.converterEmDTO(inscricaoEntity.getCandidato()));
        return inscricaoDto;
    }

    private InscricaoEntity convertToEntity(InscricaoCreateDTO inscricaoCreateDTO) {
        InscricaoEntity inscricaoEntity = objectMapper.convertValue(inscricaoCreateDTO, InscricaoEntity.class);
        return inscricaoEntity;
    }

    public InscricaoEntity convertToEntity(InscricaoDTO inscricaoDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        InscricaoEntity inscricaoEntity = objectMapper.convertValue(inscricaoDTO, InscricaoEntity.class);
        inscricaoEntity.setCandidato(candidatoService.convertToEntity(inscricaoDTO.getCandidato()));
        return inscricaoEntity;
    }
}
