package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.SendEmailDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.enums.TipoEmail;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.CandidatoFactory;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private freemarker.template.Configuration fmConfiguration;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private MimeMessage mimeMessage;

    private String from = "teste_mockito@email.com";


    @Before
    public void init() {
        ReflectionTestUtils.setField(emailService, "from", from);
    }

    @Test
    public void deveTestarSendEmailComSucesso() throws IOException, RegraDeNegocioException {
        SendEmailDTO sendEmailDTO = new SendEmailDTO();
        sendEmailDTO.setNome("nome");
        sendEmailDTO.setEmail("email@email.com");
        sendEmailDTO.setUrlToken("AS*(&!@!#NPASDA*(H&!@!#PH");

        Template template = new Template("t", Reader.nullReader());

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(anyString())).thenReturn(template);

        emailService.sendEmail(sendEmailDTO, TipoEmail.APROVADO);

        verify(emailSender,times(1)).send(mimeMessage);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarSendEmailComIOException() throws RegraDeNegocioException, MessagingException, IOException {

        SendEmailDTO sendEmailDTO = new SendEmailDTO();
        sendEmailDTO.setNome("admin");
        sendEmailDTO.setEmail("email@@");
        sendEmailDTO.setUrlToken("AS*(&!@!#NPASDA*(H&!@!#PH");

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendEmail(sendEmailDTO,TipoEmail.INSCRICAO);
    }

    @Test
    public void deveTestarGeContentFromTemplateComSucesso() throws IOException, TemplateException {

        Template template = new Template("", Reader.nullReader());
        CandidatoDTO candidatoDto = CandidatoFactory.getCandidatoDTO();

        SendEmailDTO sendEmailDto = new SendEmailDTO();
        sendEmailDto.setNome(candidatoDto.getNome());
        sendEmailDto.setEmail(candidatoDto.getEmail());
        sendEmailDto.setUrlToken("www.ols.com.br");

        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", candidatoDto.getNome());
        dados.put("email", from);
        dados.put("msg1", "Mensagem1");
        dados.put("msg2", "Mensagem2");
        dados.put("msg3", "Mensagem3");
        TipoEmail tipoEmail = TipoEmail.REPROVADO;

        when(fmConfiguration.getTemplate(anyString())).thenReturn(template);
        String geContenteFromTemplateRetorno = emailService.geContentFromTemplate(sendEmailDto, tipoEmail);

        assertNotNull(geContenteFromTemplateRetorno);
    }

    @Test
    public void deveTestarGeContentFromTemplateRecoverComSucesso() throws IOException, TemplateException {

        Template template = new Template("",Reader.nullReader());
        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
        SendEmailDTO sendEmailDto = new SendEmailDTO();
        sendEmailDto.setNome(candidatoDTO.getNome());
        sendEmailDto.setEmail(candidatoDTO.getEmail());
        sendEmailDto.setUrlToken("www.ols.com.br");

        TipoEmail tipoEmail = TipoEmail.RECOVER_PASSWORD;

        when(fmConfiguration.getTemplate(anyString())).thenReturn(template);
        String geContentFromTemplateRetorno = emailService.geContentFromTemplate(sendEmailDto,tipoEmail);

        assertNotNull(geContentFromTemplateRetorno);
    }
}
