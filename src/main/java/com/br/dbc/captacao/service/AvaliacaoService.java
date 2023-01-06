package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.gestor.GestorCreateDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.*;
import com.br.dbc.captacao.enums.Genero;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.AvaliacaoRepository;
import com.br.dbc.captacao.repository.CargoRepository;
import com.br.dbc.captacao.repository.GestorRepository;
import com.br.dbc.captacao.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private static final String CHAVE_CARGOS = "cargos";
    private static final String CHAVE_LOGIN = "username";
    @Value("${jwt.secret}")
    private String secret;
    private static final int DESCENDING = 1;
    private final ObjectMapper objectMapper;
    private final AvaliacaoRepository avaliacaoRepository;
    private final InscricaoService inscricaoService;

    private final TrilhaService trilhaService;

    private final EdicaoService edicaoService;

    private final GestorRepository gestorRepository;
    private final GestorService gestorService;

    private final CargoService cargoService;


    public AvaliacaoDTO create(AvaliacaoCreateDTO avaliacaoCreateDTO, String token) throws RegraDeNegocioException {
        if (avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(avaliacaoCreateDTO.getIdInscricao() )!= null) {
            throw new RegraDeNegocioException("Candidato já avaliado!");
        }

        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity();
        InscricaoEntity inscricao = inscricaoService.findById(avaliacaoCreateDTO.getIdInscricao());
        avaliacaoEntity.setAprovado(avaliacaoCreateDTO.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        avaliacaoEntity.setInscricao(inscricao);

        GestorEntity gestor = getUser(token);
        avaliacaoEntity.setAvaliador(gestor);
        AvaliacaoEntity avaliacaoRetorno = avaliacaoRepository.save(avaliacaoEntity);
        AvaliacaoDTO avaliacaoDto = convertToDTO(avaliacaoRetorno);
        GestorDTO gestorDTO = objectMapper.convertValue(gestor, GestorDTO.class);
        avaliacaoDto.setAvaliador(gestorDTO);

        List<CargoDTO> cargoDTOList = new ArrayList<>();
        for (CargoEntity cargo : gestor.getCargoEntity()) {
            CargoDTO cargoDTO = objectMapper.convertValue(cargo, CargoDTO.class);
            cargoDTO.setId(cargo.getIdCargo());
            cargoDTOList.add(cargoDTO);
        }
        gestorDTO.setCargosDto(cargoDTOList);

        FormularioDTO formularioDTO = objectMapper.convertValue(inscricao.getCandidato().getFormularioEntity(), FormularioDTO.class);

        List<TrilhaDTO> trilhaDTOList = new ArrayList<>();
        for (TrilhaEntity trilhaTemp : avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity().getTrilhaEntitySet()) {
            trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
        }
        formularioDTO.setTrilhas(new HashSet<>(trilhaDTOList));

        avaliacaoDto.getInscricao().getCandidato().setFormulario(formularioDTO);

        inscricaoService.setAvaliado(avaliacaoCreateDTO.getIdInscricao());
        return avaliacaoDto;
    }

    protected GestorEntity getUser(String token) {

        token = token.replace("Bearer ", "");

        Claims chaves = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        String email = chaves.get(CHAVE_LOGIN , String.class);
        List<String> cargos = chaves.get(CHAVE_CARGOS, List.class);

        Set<CargoEntity> lista = cargos.stream().map(x -> {
            try {
                return cargoService.findByNome(x);
            } catch (RegraDeNegocioException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());


        GestorEntity gestor = gestorRepository.findByEmail(email);
        if (gestor != null){
            gestor.setEmail(email);
            gestor.setNome(email.replace(".", " "));
            gestor.setSenha("123456789");
            gestor.setAtivo(TipoMarcacao.T);
            gestor.setCargoEntity(lista);

            gestor = gestorRepository.save(gestor);
        }

        return gestor;
    }


    public PageDTO<AvaliacaoDTO> list(Integer pagina, Integer tamanho, String sort, int order) {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<AvaliacaoEntity> paginaAvaliacaoEntities = avaliacaoRepository.findAll(pageRequest);

        List<AvaliacaoDTO> avaliacaoDtos = paginaAvaliacaoEntities.getContent().stream()
                .map(this::convertToDTO).toList();

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
        avaliacaoDTO.setAvaliador(objectMapper.convertValue(avaliacaoEntity.getAvaliador(), GestorDTO.class));
        avaliacaoDTO.setInscricao(inscricaoService.converterParaDTO(avaliacaoEntity.getInscricao()));
        return avaliacaoDTO;
    }

    public AvaliacaoEntity convertToEntity(AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        AvaliacaoEntity avaliacaoEntity = objectMapper.convertValue(avaliacaoCreateDTO, AvaliacaoEntity.class);
        InscricaoEntity inscricaoEntity = inscricaoService.convertToEntity(inscricaoService.findDtoByid(avaliacaoCreateDTO.getIdInscricao()));
        avaliacaoEntity.setInscricao(inscricaoEntity);
        avaliacaoEntity.setAprovado(avaliacaoCreateDTO.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        avaliacaoEntity.setAvaliador(gestorService.convertToEntity(gestorService.findDtoById(1)));
        return avaliacaoEntity;
    }

    public List<AvaliacaoDTO> listByTrilha(String trilha) throws RegraDeNegocioException {

        TrilhaEntity trilhaEntity = trilhaService.findByNome(trilha);

        List<AvaliacaoDTO> avaliacaoDTOListByTrilha = avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_FormularioEntity_TrilhaEntitySet(trilhaEntity).stream()
                .map(avaliacaoEntity -> {

                    AvaliacaoDTO avaliacaoDTO = objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class);
                    InscricaoDTO inscricaoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao(), InscricaoDTO.class);
                    CandidatoDTO candidatoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato(), CandidatoDTO.class);

                    List<CargoDTO> cargoDTOList = new ArrayList<>();
                    for (CargoEntity cargo : avaliacaoEntity.getAvaliador().getCargoEntity()) {
                        CargoDTO cargoDTO = objectMapper.convertValue(cargo, CargoDTO.class);
                        cargoDTO.setId(cargo.getIdCargo());
                        cargoDTOList.add(cargoDTO);
                    }
                    avaliacaoDTO.getAvaliador().setCargosDto(cargoDTOList);

                    List<LinguagemDTO> linguagemDTOList = new ArrayList<>();
                    for (LinguagemEntity linguagem : avaliacaoEntity.getInscricao().getCandidato().getLinguagens()) {
                        linguagemDTOList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
                    }
                    candidatoDTO.setLinguagens(linguagemDTOList);

                    candidatoDTO.setFormulario(objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity(), FormularioDTO.class));

                    List<TrilhaDTO> trilhaDTOList = new ArrayList<>();
                    for (TrilhaEntity trilhaTemp : avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity().getTrilhaEntitySet()) {
                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
                    }
                    candidatoDTO.getFormulario().setTrilhas(new HashSet<>(trilhaDTOList));

                    candidatoDTO.setEdicao(objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato().getEdicao(), EdicaoDTO.class));
                    inscricaoDTO.setCandidato(candidatoDTO);

                    avaliacaoDTO.setInscricao(inscricaoDTO);

                    return avaliacaoDTO;
                })
                .toList();

        return avaliacaoDTOListByTrilha;
    }


    public List<AvaliacaoDTO> listByEdicao(String edicao) throws RegraDeNegocioException {

        EdicaoEntity edicaoEntity = edicaoService.findByNome(edicao);

        List<AvaliacaoDTO> avaliacaoDTOListByEdicao = avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_Edicao(edicaoEntity).stream()
                .map(avaliacaoEntity -> {

                    AvaliacaoDTO avaliacaoDTO = objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class);
                    InscricaoDTO inscricaoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao(), InscricaoDTO.class);
                    CandidatoDTO candidatoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato(), CandidatoDTO.class);

                    List<CargoDTO> cargoDTOList = new ArrayList<>();
                    for (CargoEntity cargo : avaliacaoEntity.getAvaliador().getCargoEntity()) {
                        CargoDTO cargoDTO = objectMapper.convertValue(cargo, CargoDTO.class);
                        cargoDTO.setId(cargo.getIdCargo());
                        cargoDTOList.add(cargoDTO);
                    }
                    avaliacaoDTO.getAvaliador().setCargosDto(cargoDTOList);

                    List<LinguagemDTO> linguagemDTOList = new ArrayList<>();
                    for (LinguagemEntity linguagem : avaliacaoEntity.getInscricao().getCandidato().getLinguagens()) {
                        linguagemDTOList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
                    }
                    candidatoDTO.setLinguagens(linguagemDTOList);

                    candidatoDTO.setFormulario(objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity(), FormularioDTO.class));

                    List<TrilhaDTO> trilhaDTOList = new ArrayList<>();
                    for (TrilhaEntity trilhaTemp : avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity().getTrilhaEntitySet()) {
                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
                    }
                    candidatoDTO.getFormulario().setTrilhas(new HashSet<>(trilhaDTOList));

                    candidatoDTO.setEdicao(objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato().getEdicao(), EdicaoDTO.class));
                    inscricaoDTO.setCandidato(candidatoDTO);

                    avaliacaoDTO.setInscricao(inscricaoDTO);

                    return avaliacaoDTO;
                })
                .toList();

        return avaliacaoDTOListByEdicao;
    }

    public PageDTO<AvaliacaoDTO> filtroAvaliacao(Integer pagina, Integer tamanho, String email, String edicao, String trilha) throws RegraDeNegocioException{
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);

        Page<AvaliacaoEntity> avaliacaoEntityPage = avaliacaoRepository.filtrarAvaliacoes(pageRequest, email, edicao, trilha);

        List<AvaliacaoDTO> avaliacaoDTOS = avaliacaoEntityPage.stream()
                .map(avaliacaoEntity -> {
                    AvaliacaoDTO avaliacaoDTO = objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class);
                    InscricaoDTO inscricaoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao(), InscricaoDTO.class);
                    CandidatoDTO candidatoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato(), CandidatoDTO.class);
                    inscricaoDTO.setCandidato(candidatoDTO);
                    avaliacaoDTO.setInscricao(inscricaoDTO);
                    avaliacaoDTO.setAprovado(avaliacaoEntity.getAprovado());
                    avaliacaoDTO.setIdAvaliacao(avaliacaoEntity.getIdAvaliacao());
                    avaliacaoDTO.setAvaliador(objectMapper.convertValue(avaliacaoEntity.getAvaliador(), GestorDTO.class));
                    return avaliacaoDTO;
                }).toList();

        return new PageDTO<>(avaliacaoEntityPage.getTotalElements(),
                avaliacaoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                avaliacaoDTOS);
    }

}