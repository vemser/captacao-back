package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.CurriculoEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CurriculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurriculoService {

    private final CurriculoRepository curriculoRepository;

    private final CandidatoService candidatoService;

    public CurriculoEntity findById(Integer idCurriculo) throws RegraDeNegocioException {
        return curriculoRepository.findById(idCurriculo)
                .orElseThrow(() -> new RegraDeNegocioException("Currículo não encontrado!"));
    }

//    public void arquivarCurriculo(MultipartFile file, String email) throws IOException, RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(email);
//        Optional<CurriculoEntity> curriculoEntityOptional = findByCandidato(candidatoEntity);
//        String nomeArquivo = StringUtils.cleanPath(file.getOriginalFilename());
//        if (!nomeArquivo.endsWith(".pdf")) {
//            throw new RegraDeNegocioException("Formato de arquivo inválido! Inserir pdf");
//        } else {
//            if (curriculoEntityOptional.isPresent()) {
//                curriculoEntityOptional.get().setNome(nomeArquivo);
//                curriculoEntityOptional.get().setTipo(file.getContentType());
//                curriculoEntityOptional.get().setDado(file.getBytes());
//                curriculoEntityOptional.get().setCandidato(candidatoEntity);
//                curriculoRepository.save(curriculoEntityOptional.get());
//            } else {
//                CurriculoEntity curriculo = new CurriculoEntity();
//                curriculo.setNome(nomeArquivo);
//                curriculo.setTipo(file.getContentType());
//                curriculo.setDado(file.getBytes());
//                curriculo.setCandidato(candidatoEntity);
//                curriculoRepository.save(curriculo);
//            }
//        }
//    }

//    public String pegarCurriculoCandidato(String email) throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(email);
//        Optional<CurriculoEntity> curriculo = curriculoRepository.findByCandidato(candidatoEntity);
//        if (curriculo.isEmpty()) {
//            throw new RegraDeNegocioException("Usuário não possui currículo cadastrado.");
//        }
//        return Base64Utils.encodeToString(curriculo.get().getDado());
//    }

    private Optional<CurriculoEntity> findByCandidato(CandidatoEntity candidatoEntity) {
        return curriculoRepository.findByCandidato(candidatoEntity);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = candidatoService.findById(id);
        Optional<CurriculoEntity> curriculo = findByCandidato(candidatoEntity);
        Integer idCurriculo = curriculo.get().getIdCurriculo();;
        curriculoRepository.deleteById(idCurriculo);
    }
}
