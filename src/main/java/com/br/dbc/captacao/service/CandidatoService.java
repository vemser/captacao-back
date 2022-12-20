//package com.br.dbc.captacao.service;
//
//import com.br.dbc.captacao.dto.CandidatoCreateDTO;
//import com.br.dbc.captacao.dto.CandidatoDTO;
//import com.br.dbc.captacao.entity.CandidatoEntity;
//import com.br.dbc.captacao.exception.RegraDeNegocioException;
//import com.br.dbc.captacao.repository.CandidatoRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CandidatoService {
//
//    private static final int DESCENDING = 1;
//    private final CandidatoRepository candidatoRepository;
////    private final FormularioService formularioService;
//    private final ObjectMapper objectMapper;
//
//
//    public CandidatoDTO cadastro(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
////        if (!candidatoRepository.findCandidatoEntitiesByEmail(candidatoCreateDTO.getEmail()).isEmpty()) {
//            throw new RegraDeNegocioException("Email já cadastrado");
////        }
////        if (!candidatoRepository.findCandidatoEntitiesByFormulario_IdFormulario(candidatoCreateDto.getIdFormulario()).isEmpty()) {
//            throw new RegraDeNegocioException("Formulario cadastrado para outro candidato");
//        }
//        CandidatoEntity candidatoEntity = convertToEntity(candidatoCreateDTO);
//        CandidatoDTO candidatoDTO = convertToDto(candidatoRepository.save(candidatoEntity));
////        candidatoDto.setFormulario(formularioService.convertToDto(candidatoEntity.getFormulario()));
//        return candidatoDTO;
//    }
//
//
//    public void deleteById(Integer idCandidato) throws RegraDeNegocioException {
//        findById(idCandidato);
//        candidatoRepository.deleteById(idCandidato);
//    }
//
//    public CandidatoDto update(Integer idCandidato, CandidatoCreateDto candidatoCreateDto) throws RegraDeNegocioException {
//        findById(idCandidato);
//        CandidatoEntity candidatoEntity = convertToEntity(candidatoCreateDto);
//        candidatoEntity.setIdCandidato(idCandidato);
//        CandidatoEntity candidatoEntity1 = candidatoRepository.save(candidatoEntity);
//        return convertToDto(candidatoEntity1);
//    }
//
//    public CandidatoEntity findById(Integer idCandidato) throws RegraDeNegocioException {
//        return candidatoRepository.findById(idCandidato)
//                .orElseThrow(() -> new RegraDeNegocioException("Erro ao buscar candidato!"));
//    }
//
//    public CandidatoDto findDtoById(Integer idCandidato) throws RegraDeNegocioException {
//        return convertToDto(findById(idCandidato));
//    }
//
//    public List<CandidatoDto> findCandidatoDtoByEmail(String email) throws RegraDeNegocioException {
//        List<CandidatoEntity> candidato = candidatoRepository.findCandidatoEntitiesByEmail(email);
//        List<CandidatoDto> candidatoDto = candidato.stream().map(candidatoEntity -> convertToDto(candidatoEntity)).toList();
//        return candidatoDto;
//    }
//
//    public PageDto<CandidatoDto> listaAllPaginado(Integer pagina, Integer tamanho, String sort, int order) {
//        Sort ordenacao = Sort.by(sort).ascending();
//        if (order == DESCENDING) {
//            ordenacao = Sort.by(sort).descending();
//        }
//        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
//        Page<CandidatoEntity> paginaCandidatoEntity = candidatoRepository.findAll(pageRequest);
//
//        List<CandidatoDto> candidatoDtos = paginaCandidatoEntity.getContent().stream()
//                .map(candidatoEntity -> {
//                    CandidatoDto candidatoDto = convertToDto(candidatoEntity);
//                    return candidatoDto;
//                }).toList();
//
//        return new PageDto<>(paginaCandidatoEntity.getTotalElements(),
//                paginaCandidatoEntity.getTotalPages(),
//                pagina,
//                tamanho,
//                candidatoDtos);
//    }
//
//
//    public CandidatoDto convertToDto(CandidatoEntity candidatoEntity) {
//        CandidatoDto candidatoDto = objectMapper.convertValue(candidatoEntity, CandidatoDto.class);
//        candidatoDto.setFormulario(formularioService.convertToDto(candidatoEntity.getFormulario()));
//        return candidatoDto;
//    }
//
//    private CandidatoEntity convertToEntity(CandidatoCreateDto candidatoCreateDto) throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoCreateDto, CandidatoEntity.class);
//        candidatoEntity.setPcd(candidatoCreateDto.isPcdboolean() ? TipoMarcacao.T : TipoMarcacao.F);
//        candidatoEntity.setFormulario(formularioService.convertToEntity(formularioService.findDtoById(candidatoCreateDto.getIdFormulario())));
//        return candidatoEntity;
//    }
//
//    public CandidatoEntity convertToEntity(CandidatoDto candidatoDto) {
//        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoDto, CandidatoEntity.class);
//        candidatoEntity.setFormulario(formularioService.convertToEntity(candidatoDto.getFormulario()));
//        return candidatoEntity;
//    }
//
//}