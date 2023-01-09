package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.dto.gestor.GestorCreateDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.gestor.GestorEmailNomeCargoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CargoEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.GestorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GestorService {

    private static final String CHAVE_CARGOS = "cargos";
    private static final String CHAVE_LOGIN = "username";
    @Value("${jwt.secret}")
    private String secret;

    private static final int DESCENDING = 1;
    private final GestorRepository gestorRepository;
    private final CargoService cargoService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
//    private final TokenService tokenService;

    private final EmailService emailService;

    private static final TipoMarcacao USUARIO_ATIVO = TipoMarcacao.T;

    private static final TipoMarcacao USUARIO_INATIVO = TipoMarcacao.F;


//    public GestorDTO cadastrar(GestorCreateDTO gestorCreateDTO) throws RegraDeNegocioException {
//        if (!gestorCreateDTO.getEmail().endsWith("@dbccompany.com.br")) {
//            throw new RegraDeNegocioException("Email não valido!");
//        }
//        GestorEntity gestorEntity = convertToEntity(gestorCreateDTO);
//        gestorEntity.setAtivo(TipoMarcacao.T);
//        gestorEntity.setSenha(passwordEncoder.encode(gestorCreateDTO.getSenha()));
//        return convertoToDTO(gestorRepository.save(gestorEntity));
//    }

    public PageDTO<GestorDTO> listar(Integer pagina, Integer tamanho, String sort, int order) {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<GestorEntity> paginaGestorEntities = gestorRepository.findAll(pageRequest);
        List<GestorDTO> gestorDTOS = paginaGestorEntities.getContent().stream()
                .map(gestorEntity -> {
                    GestorDTO gestorDTO = new GestorDTO();
                    gestorDTO.setIdGestor(gestorEntity.getIdGestor());
                    gestorDTO.setNome(gestorEntity.getNome());
                    gestorDTO.setEmail(gestorEntity.getEmail());
                    gestorDTO.setAtivo(gestorEntity.getAtivo());
                    List<CargoDTO> lista = new ArrayList<>();
                    gestorEntity.getCargoEntity().stream()
                            .forEach(cargoEntity -> {
                                CargoDTO cargoDTO = objectMapper.convertValue(cargoEntity, CargoDTO.class);
                                cargoDTO.setId(cargoEntity.getIdCargo());
                                lista.add(cargoDTO);
                            });
                    gestorDTO.setCargosDto(new ArrayList<>(lista));
                    return gestorDTO;
                }).toList();
        return new PageDTO<>(paginaGestorEntities.getTotalElements(),
                paginaGestorEntities.getTotalPages(),
                pagina,
                tamanho,
                gestorDTOS);
    }

    public GestorDTO findDtoById(Integer idGestor) throws RegraDeNegocioException {
        GestorEntity gestorEntity = findById(idGestor);
        GestorDTO gestorDTO = convertoToDTO(gestorEntity);
        return getGestorDTO(gestorEntity);
    }

    public GestorEntity findById(Integer id) throws RegraDeNegocioException {
        return gestorRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Usuario não encontrado!"));
    }


//    public GestorDTO editar(Integer id, GestorCreateDTO gestorCreateDTO) throws RegraDeNegocioException {
//        if (id != getIdLoggedUser() && findById(getIdLoggedUser()).getCargoEntity().getNome() == "ROLE_COLABORADOR") {
//            throw new RegraDeNegocioException("Você não tem permissão para editar esse gestor.");
//        }
//        GestorEntity gestorEntity = findById(id);
//        gestorEntity.setCargoEntity(cargoService.findById(gestorCreateDTO.getTipoCargo()));
//        gestorEntity.setNome(gestorCreateDTO.getNome());
//        gestorEntity.setEmail(gestorCreateDTO.getEmail());
//        if (!gestorCreateDTO.getSenha().isBlank()) {
//            gestorEntity.setSenha(passwordEncoder.encode(gestorCreateDTO.getSenha()));
//        }
//        gestorRepository.save(gestorEntity);
//        GestorDTO gestorDTO = convertoToDTO(gestorEntity);
//        return gestorDTO;
//
//    }

//    public GestorDTO editarSenha(Integer id, GestorSenhaDTO gestorSenhaDTO) throws RegraDeNegocioException {
//        GestorEntity gestorEntity = findById(id);
//        gestorEntity.setSenha(passwordEncoder.encode(gestorSenhaDTO.getSenha()));
//        gestorRepository.save(gestorEntity);
//        GestorDTO gestorDTO = convertoToDTO(gestorEntity);
//        return gestorDTO;
//
//    }

    public List<GestorDTO> findGestorbyNomeOrEmail(GestorEmailNomeCargoDTO gestorEmailNomeCargoDTO) throws RegraDeNegocioException {
        if (gestorEmailNomeCargoDTO.getNome().isBlank() && gestorEmailNomeCargoDTO.getEmail().isBlank()) {
            return Collections.emptyList();
        }
        CargoEntity cargo = cargoService.findById(gestorEmailNomeCargoDTO.getCargo().getId());
        List<GestorEntity> lista = gestorRepository.findGestorEntitiesByCargoEntityAndNomeIgnoreCaseOrCargoEntityAndEmailIgnoreCase(cargo, gestorEmailNomeCargoDTO.getNome(), cargo, gestorEmailNomeCargoDTO.getEmail());
        return lista.stream()
                .map(this::convertoToDTO)
                .toList();

    }

    public void remover(Integer id) throws RegraDeNegocioException {
        findById(id);
        gestorRepository.deleteById(id);
    }

    public GestorEntity findByEmail(String email) throws RegraDeNegocioException {
        return gestorRepository.findGestorEntityByEmailEqualsIgnoreCase(email)
                .orElseThrow(() -> new RegraDeNegocioException("Email não encontrado"));
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

//    public void forgotPassword(GestorEmailDTO gestorEmailDto) throws RegraDeNegocioException {
//        GestorEntity gestorEntity = findByEmail(gestorEmailDto.getEmail());
//        TokenDTO token = tokenService.getToken(gestorEntity, true);
//
//        SendEmailDTO sendEmailDto = new SendEmailDTO();
//        sendEmailDto.setEmail(gestorEntity.getEmail());
//        sendEmailDto.setNome(gestorEntity.getNome());
//        String url = gestorEmailDto.getUrl();
//        sendEmailDto.setUrlToken(url + "/forgot-password/?token=" + token.getToken());
//        emailService.sendEmail(sendEmailDto, TipoEmail.RECOVER_PASSWORD);
//    }

//    public Integer getIdLoggedUser() {
//        return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
//    }

//    public GestorDTO getLoggedUser() throws RegraDeNegocioException {
//        return convertoToDTO(findById(getIdLoggedUser()));
//    }


    public GestorDTO desativarConta(Integer idUsuario) throws RegraDeNegocioException {
        GestorEntity usuarioEncontrado = findById(idUsuario);
        usuarioEncontrado.setAtivo(USUARIO_INATIVO);
        gestorRepository.save(usuarioEncontrado);
        return convertoToDTO(usuarioEncontrado);
    }

//    public List<GestorDTO> contasInativas() {
//        return gestorRepository.findByAtivo(USUARIO_INATIVO).stream()
//                .map(gestorEntity -> convertoToDTO(gestorEntity))
//                .toList();
//    }

    public List<GestorDTO> contasInativas() {
        return gestorRepository.findByAtivo(USUARIO_INATIVO).stream()
                .map(gestorEntity -> convertoToDTO(gestorEntity))
                .toList();
    }

    public GestorDTO convertoToDTO(GestorEntity gestorEntity) {
        GestorDTO gestorDTO = objectMapper.convertValue(gestorEntity, GestorDTO.class);
        return getGestorDTO(gestorEntity);
    }

    private GestorDTO getGestorDTO(GestorEntity gestorEntity) {
        GestorDTO gestorDTO = objectMapper.convertValue(gestorEntity, GestorDTO.class);
        List<CargoDTO> listaCargos = gestorEntity.getCargoEntity().stream()
                .map(cargoEntity -> {
                    CargoDTO cargoDTO = objectMapper.convertValue(cargoEntity, CargoDTO.class);
                    cargoDTO.setId(cargoEntity.getIdCargo());
                    return cargoDTO;
                }).toList();
        gestorDTO.setCargosDto(listaCargos);
        return gestorDTO;
    }

    private GestorEntity convertToEntity(GestorCreateDTO gestorCreateDTO) throws RegraDeNegocioException {
        GestorEntity gestorEntity = objectMapper.convertValue(gestorCreateDTO, GestorEntity.class);
        Set<CargoEntity> cargo = new HashSet<>();
        cargo.add(cargoService.findById(gestorCreateDTO.getTipoCargo()));
        gestorEntity.setCargoEntity(cargo);
        return gestorEntity;
    }

    public GestorEntity convertToEntity(GestorDTO gestorDTO) {
        GestorEntity gestorEntity = objectMapper.convertValue(gestorDTO, GestorEntity.class);
        Set<CargoEntity> cargo = new HashSet<>();
        gestorDTO.getCargosDto().stream()
                .forEach(cargoDTO -> {
                    CargoEntity cargoEntity = objectMapper.convertValue(cargoDTO, CargoEntity.class);
                    cargo.add(cargoEntity);
                });
        return gestorEntity;
    }
}
