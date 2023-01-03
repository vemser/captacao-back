//package com.br.dbc.captacao.service;
//
//import com.br.dbc.captacao.dto.SendEmailDTO;
//import com.br.dbc.captacao.enums.TipoEmail;
//import com.br.dbc.captacao.exception.RegraDeNegocioException;
//import freemarker.template.Template;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import javax.mail.internet.MimeMessage;
//import java.io.IOException;
//import java.io.Reader;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class EmailServiceTest {
//
//    @InjectMocks
//    private EmailService emailService;
//
//    @Mock
//    private freemarker.template.Configuration fmConfiguration;
//
//    @Mock
//    private JavaMailSender emailSender;
//
//    @Mock
//    private MimeMessage mimeMessage;
//
//    private String from = "teste_mockito@email.com";
//
//
//    @Before
//    public void init() {
//        ReflectionTestUtils.setField(emailService, "from", from);
//    }
//
//    @Test
//    public void deveTestarSendEmailComSucesso() throws IOException, RegraDeNegocioException {
//        SendEmailDTO sendEmailDTO = new SendEmailDTO();
//        sendEmailDTO.setNome("nome");
//        sendEmailDTO.setEmail("email@email.com");
//        sendEmailDTO.setUrlToken("AS*(&!@!#NPASDA*(H&!@!#PH");
//
//        Template template = new Template("t", Reader.nullReader());
//
//        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
//        when(fmConfiguration.getTemplate(anyString())).thenReturn(template);
//
//        emailService.sendEmail(sendEmailDTO, TipoEmail.APROVADO);
//
//    }
//}
