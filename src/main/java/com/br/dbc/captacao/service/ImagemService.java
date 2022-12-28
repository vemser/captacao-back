package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.entity.ImagemEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.ImagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImagemService {

    private final CandidatoService candidatoService;
    private final ImagemRepository imagemRepository;
    private final GestorService gestorService;

    public ImagemEntity findById(Integer idImagem) throws RegraDeNegocioException {
        return imagemRepository.findById(idImagem)
                .orElseThrow(() -> new RegraDeNegocioException("Imagem não encontrada!"));
    }

    public void arquivarCandidato(MultipartFile file, String email) throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(email);
        Optional<ImagemEntity> imagemBD = findByCandidato(candidatoEntity);
        String nomeArquivo = StringUtils.cleanPath((file.getOriginalFilename()));
        if(!nomeArquivo.endsWith(".png") && !nomeArquivo.endsWith(".jpeg") && !nomeArquivo.endsWith(".jpg")){
            throw new RegraDeNegocioException("Formato de arquivo inválido! Inserir .png, .jpg ou .jpeg");
        }
        if (imagemBD.isPresent()) {
            imagemBD.get().setNome(nomeArquivo);
            imagemBD.get().setTipo(file.getContentType());
            imagemBD.get().setData(file.getBytes());
            imagemBD.get().setCandidato(candidatoEntity);
            imagemRepository.save(imagemBD.get());
        } else {
            ImagemEntity novaImagemBD = new ImagemEntity();
            novaImagemBD.setNome(nomeArquivo);
            novaImagemBD.setTipo(file.getContentType());
            novaImagemBD.setData(file.getBytes());
            novaImagemBD.setCandidato(candidatoEntity);
            imagemRepository.save(novaImagemBD);
        }
    }

    public void arquivarGestor(MultipartFile file, String email) throws RegraDeNegocioException, IOException {
        GestorEntity gestorEntity = gestorService.findByEmail(email);
        Optional<ImagemEntity> imagemBD = findByGestor(gestorEntity);
        String nomeArquivo = StringUtils.cleanPath((file.getOriginalFilename()));
        if(!nomeArquivo.endsWith(".png") && !nomeArquivo.endsWith(".jpeg") && !nomeArquivo.endsWith(".jpg")){
            throw new RegraDeNegocioException("Formato de arquivo inválido! Inserir .png, .jpg ou .jpeg");
        }
        if (imagemBD.isPresent()) {
            imagemBD.get().setNome(nomeArquivo);
            imagemBD.get().setTipo(file.getContentType());
            imagemBD.get().setData(file.getBytes());
            imagemBD.get().setGestorEntity(gestorEntity);
            imagemRepository.save(imagemBD.get());
        } else {
            ImagemEntity novaImagemBD = new ImagemEntity();
            novaImagemBD.setNome(nomeArquivo);
            novaImagemBD.setTipo(file.getContentType());
            novaImagemBD.setData(file.getBytes());
            novaImagemBD.setGestorEntity(gestorEntity);
            imagemRepository.save(novaImagemBD);
        }
    }

    public String pegarImagemCandidato(String email) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(email);
        Optional<ImagemEntity> imagemBD = findByCandidato(candidatoEntity);
        if (imagemBD.isEmpty()) {
            throw new RegraDeNegocioException("Candidato não possui imagem cadastrada.");
        }
        return Base64Utils.encodeToString(imagemBD.get().getData());
    }

    public String pegarImagemUsuario(String email) throws RegraDeNegocioException {
        GestorEntity usuarioEntity = gestorService.findByEmail(email);
        Optional<ImagemEntity> imagemBD = findByGestor(usuarioEntity);
        if (imagemBD.isEmpty()) {
            throw new RegraDeNegocioException("Usuário não possui imagem cadastrada.");
        }
        return Base64Utils.encodeToString(imagemBD.get().getData());
    }

    private Optional<ImagemEntity> findByCandidato(CandidatoEntity candidatoEntity) {
        return imagemRepository.findByCandidato(candidatoEntity);
    }

    private Optional<ImagemEntity> findByGestor(GestorEntity gestorEntity) {
        return imagemRepository.findByGestorEntity(gestorEntity);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        imagemRepository.deleteById(id);
    }
}
