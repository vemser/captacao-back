package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.SendEmailDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.AvaliacaoEntity;
import com.br.dbc.captacao.entity.InscricaoEntity;
import com.br.dbc.captacao.enums.TipoEmail;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.AvaliacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private static final int DESCENDING = 1;
    private final ObjectMapper objectMapper;
    private final AvaliacaoRepository avaliacaoRepository;
    private final InscricaoService inscricaoService;
    private final GestorService gestorService;
    private final EmailService emailService;


    public AvaliacaoDTO create(AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException {
        if (!avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(avaliacaoCreateDTO.getIdInscricao()).isEmpty()) {
            throw new RegraDeNegocioException("Formulario cadastrado para outro candidato");
        }
        AvaliacaoEntity avaliacaoEntity = convertToEntity(avaliacaoCreateDTO);
        AvaliacaoDTO avaliacaoDto = convertToDTO(avaliacaoRepository.save(avaliacaoEntity));
        avaliacaoDto.setAvaliador(gestorService.convertoToDTO(avaliacaoEntity.getAvaliador()));
        SendEmailDTO sendEmailDTO = new SendEmailDTO();
        sendEmailDTO.setNome(avaliacaoDto.getInscricao().getCandidato().getNome());
        sendEmailDTO.setEmail(avaliacaoDto.getInscricao().getCandidato().getEmail());
        if (avaliacaoDto.getAprovado() == TipoMarcacao.T) {
            emailService.sendEmail(sendEmailDTO, TipoEmail.APROVADO);
        } else {
            emailService.sendEmail(sendEmailDTO, TipoEmail.REPROVADO);
        }
        inscricaoService.setAvaliado(avaliacaoCreateDTO.getIdInscricao());
        return avaliacaoDto;
    }


    public PageDTO<AvaliacaoDTO> list(Integer pagina, Integer tamanho, String sort, int order) {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<AvaliacaoEntity> paginaAvaliacaoEntities = avaliacaoRepository.findAll(pageRequest);

        List<AvaliacaoDTO> avaliacaoDtos = paginaAvaliacaoEntities.getContent().stream()
                .map(avaliacaoEntity -> convertToDTO(avaliacaoEntity)).toList();

        return new PageDTO<>(paginaAvaliacaoEntities.getTotalElements(),
                paginaAvaliacaoEntities.getTotalPages(),
                pagina,
                tamanho,
                avaliacaoDtos);
    }


    public AvaliacaoDTO update(Integer idAvaliacao, AvaliacaoCreateDTO avaliacaoCreateDto) throws RegraDeNegocioException {
        AvaliacaoEntity avaliacaoEntity = findById(idAvaliacao);
        avaliacaoEntity.setAprovado(avaliacaoCreateDto.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        AvaliacaoDTO avaliacaoRetorno = convertToDTO(avaliacaoRepository.save(avaliacaoEntity));
        return avaliacaoRetorno;
    }

    public AvaliacaoDTO findDtoById(Integer idAvaliacao) throws RegraDeNegocioException {
        return convertToDTO(findById(idAvaliacao));
    }

    public void deleteById(Integer idAvaliacao) throws RegraDeNegocioException {
        findById(idAvaliacao);
        avaliacaoRepository.deleteById(idAvaliacao);
    }

    private AvaliacaoEntity findById(Integer idAvaliacao) throws RegraDeNegocioException {
        return avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new RegraDeNegocioException("Avaliação não encontrada!"));
    }

    public List<AvaliacaoDTO> findAvaliacaoByCanditadoEmail(String email) {
        List<AvaliacaoEntity> lista = avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_Email(email);
        return lista.stream().map(avaliacaoEntity -> convertToDTO(avaliacaoEntity))
                .toList();
    }


    public AvaliacaoDTO convertToDTO(AvaliacaoEntity avaliacaoEntity) {
        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setIdAvaliacao(avaliacaoEntity.getIdAvaliacao());
        avaliacaoDTO.setAprovado(avaliacaoEntity.getAprovado());
        avaliacaoDTO.setAvaliador(gestorService.convertoToDTO(avaliacaoEntity.getAvaliador()));
        avaliacaoDTO.setInscricao(inscricaoService.converterParaDTO(avaliacaoEntity.getInscricao()));
        return avaliacaoDTO;
    }

    public AvaliacaoEntity convertToEntity(AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException {
        AvaliacaoEntity avaliacaoEntity = objectMapper.convertValue(avaliacaoCreateDTO, AvaliacaoEntity.class);
        InscricaoEntity inscricaoEntity = inscricaoService.convertToEntity(inscricaoService.findDtoByid(avaliacaoCreateDTO.getIdInscricao()));
        avaliacaoEntity.setInscricao(inscricaoEntity);
        avaliacaoEntity.setAprovado(avaliacaoCreateDTO.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        avaliacaoEntity.setAvaliador(gestorService.convertToEntity(gestorService.findDtoById(1)));
        return avaliacaoEntity;
    }

}