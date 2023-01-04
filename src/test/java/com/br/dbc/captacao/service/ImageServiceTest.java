package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.entity.ImagemEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.GestorFactory;
import com.br.dbc.captacao.factory.ImageFactory;
import com.br.dbc.captacao.repository.ImagemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HexFormat;
import java.util.Optional;

import static com.br.dbc.captacao.factory.CandidatoFactory.getCandidatoEntity;
import static com.br.dbc.captacao.factory.ImageFactory.getImageEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

    @InjectMocks
    private ImagemService imageService;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private ImagemRepository imageRepository;

    @Mock
    private GestorService usuarioService;

    @Test
    public void deveTestarArquivarCandidatoComSucesso() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem.png","imagem.png",".png",imagemBytes);

        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(imageRepository.save(any())).thenReturn(getImageEntity());

        imageService.arquivarCandidato(imagem, candidatoEntity.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarArquivarCandidatoComImagemComSucesso() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem.png","imagem.png",".png",imagemBytes);
        ImagemEntity imagemEntity = getImageEntity();
        candidatoEntity.setImageEntity(imagemEntity);

        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.of(getImageEntity()));
        when(imageRepository.save(any())).thenReturn(getImageEntity());

        imageService.arquivarCandidato(imagem, candidatoEntity.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarUploadImagemCandidatoCasoExistaComErro() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);
        candidatoEntity.setImageEntity(ImageFactory.getImageEntity());

        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.of(getImageEntity()));

        imageService.arquivarCandidato(imagem, candidatoEntity.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarUploadUsuarioCasoExistaImagemComSucesso() throws RegraDeNegocioException, IOException {
        GestorEntity usuario = GestorFactory.getGestorEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem.png","imagem.png",".png",imagemBytes);


        when(usuarioService.findByEmail(any())).thenReturn(usuario);
        when(imageRepository.findByGestorEntity(any())).thenReturn(Optional.of(getImageEntity()));
        when(imageRepository.save(any())).thenReturn(ImageFactory.getImageUsuario());

        imageService.arquivarGestor(imagem, usuario.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test
    public void testarUploadImagemUsuarioCasoNaoExistaComSucesso() throws RegraDeNegocioException, IOException {
        GestorEntity usuario = GestorFactory.getGestorEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem.png","imagem.png",".png",imagemBytes);

        when(usuarioService.findByEmail(any())).thenReturn(usuario);
        when(imageRepository.findByGestorEntity(any())).thenReturn(Optional.empty());
        when(imageRepository.save(any())).thenReturn(getImageEntity());

        imageService.arquivarGestor(imagem, usuario.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test
    public void devePegarImagemCandidatoComSucesso() throws RegraDeNegocioException {
        //Setup
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(2);
        candidatoEntity.setEmail("teste@gmail.com.br");
        //Act
        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.of(getImageEntity()));
        String imagemBase64 = imageService.pegarImagemCandidato(candidatoEntity.getEmail());
        //Assert
        assertNotNull(imagemBase64);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemCandidatoComErro() throws RegraDeNegocioException {
        //Setup
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        //Act
        when(candidatoService.findByEmailEntity(anyString())).thenReturn(candidatoEntity);
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.empty());
        String imagemBase64 = imageService.pegarImagemCandidato(candidatoEntity.getEmail());
    }


    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarArquivarGestorComExcpetion() throws RegraDeNegocioException, IOException {
        GestorEntity usuarioEntity = GestorFactory.getGestorEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);

        when(usuarioService.findByEmail(any())).thenReturn(usuarioEntity);
        when(imageRepository.findByGestorEntity(any())).thenReturn(Optional.of(getImageEntity()));

        imageService.arquivarGestor(imagem, usuarioEntity.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemCandidatoComEmailComErro() throws RegraDeNegocioException {
        //Setup
        final String emailEsperado = "abc@dbccompany.com.br";
        //Act
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.empty());
        imageService.pegarImagemCandidato(emailEsperado);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemUsuarioComEmailComErro() throws RegraDeNegocioException {
        //Setup
        final String emailEsperado = "abc@dbccompany.com.br";
        //Act
        imageService.pegarImagemUsuario(emailEsperado);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemUsuarioImagemComErro() throws RegraDeNegocioException {
        //Setup
        final Integer id = 2;
        //Act
        when(imageRepository.findById(anyInt())).thenReturn(Optional.empty());
        //Assert
        imageService.findById(2);
    }

    @Test
    public void devePegarImagemUsuarioComSucesso() throws RegraDeNegocioException {
        //Setup
        GestorEntity usuarioEntity = GestorFactory.getGestorEntity();
        //Act
        when(usuarioService.findByEmail(any())).thenReturn(usuarioEntity);
        when(imageRepository.findByGestorEntity(any())).thenReturn(Optional.of(getImageEntity()));
        String imagemBase64 = imageService.pegarImagemUsuario(usuarioEntity.getEmail());
        //Assert
        assertNotNull(imagemBase64);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        //Setup
        Integer id = 1;
        //Act
        when(imageRepository.findById(anyInt())).thenReturn(Optional.of(getImageEntity()));
        ImagemEntity imageEntity = imageService.findById(id);
        //Assert
        assertNotNull(imageEntity);
        assertEquals(imageEntity.getIdImagem(), id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarFindByIdComErro() throws RegraDeNegocioException {
        //Setup
        Integer id = 2;
        //Act
        when(imageRepository.findById(anyInt())).thenReturn(Optional.empty());
        //Assert
        imageService.findById(id);
    }

    @Test
    public void testarDeleteFisicoComSucesso() throws RegraDeNegocioException {
        final Integer id = 1;
        when(imageRepository.findById(anyInt())).thenReturn(Optional.of(getImageEntity()));
        imageService.deleteFisico(id);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarDeleteFisicoComErro() throws RegraDeNegocioException {
        final Integer id = 2;

        imageService.deleteFisico(id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoUsuarioNaoTiverFotoCadastrada() throws RegraDeNegocioException {
        final String email = "Heloise Isabela Lopes";

        GestorEntity usuarioEntity = GestorFactory.getGestorEntity();

        when(usuarioService.findByEmail(any())).thenReturn(usuarioEntity);

        imageService.pegarImagemUsuario(email);
    }
}