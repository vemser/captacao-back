package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.SendEmailDTO;
import com.br.dbc.captacao.entity.EntrevistaEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.enums.TipoEmail;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final freemarker.template.Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender emailSender;

    public void sendEmail(SendEmailDTO sendEmailDTO, TipoEmail tipoEmail) throws RegraDeNegocioException {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(sendEmailDTO.getEmail());
            mimeMessageHelper.setSubject(tipoEmail.getSubject());
            mimeMessageHelper.setText(geContentFromTemplate(sendEmailDTO, tipoEmail), true);

            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RegraDeNegocioException("Erro ao enviar email");
        }
    }

    public void sendEmailConfirmacaoEntrevista(EntrevistaEntity entrevistaEntity, String email, String token) throws RegraDeNegocioException {
        final String subject = "Confirmação de entrevista.";
        sendEmailEntrevista(entrevistaEntity, email, token, "envio-entrevista-template.html", subject);
    }

    public void sendEmailEntrevista(EntrevistaEntity entrevistaEntity, String email, String token, String nomeTemplate, String assunto) throws RegraDeNegocioException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(getContentFromTemplateEntrevista(entrevistaEntity.getDataEntrevista(), entrevistaEntity.getCandidatoEntity().getNome(), nomeTemplate, token), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RegraDeNegocioException("Email inválido! inserir outro e-mail.");
        }
    }

    public String getContentFromTemplateEntrevista(LocalDateTime data, String nome, String nomeTemplate, String token) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", from);
        dados.put("nome", nome);
        dados.put("data", data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        dados.put("token", "http://vemser-dbc.dbccompany.com.br:39000/vemser/captacao-front/confirmar-entrevista?tokenEntrevista="+token);
        dados.put("colaborador", from);
        Template template = fmConfiguration.getTemplate(nomeTemplate);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public String geContentFromTemplate(SendEmailDTO sendEmailDTO, TipoEmail tipoEmail) throws IOException, TemplateException {

        final String MESSAGE_DUVIDA = "Em caso de dúvidas, nos contate através do e-mail <br> <strong style='color:white'> VemSer@dbccompany.com.br </strong>";

        String base = tipoEmail.getDescricao();
        if (tipoEmail.equals(TipoEmail.RECOVER_PASSWORD)) {
            String urlToken = sendEmailDTO.getUrlToken();
            base = base.replace("/url/", urlToken);
        }

        Map<String, Object> dados = new HashMap<>();


        dados.put("data", sendEmailDTO.getData());


        dados.put("nome", sendEmailDTO.getNome());
        dados.put("email", from);
        dados.put("msg1", "Agradecemos o teu interesse em fazer parte do time DBC Company e participar desta edição do Vem Ser DBC. :)");
        dados.put("msg2", base);
        dados.put("msg3", MESSAGE_DUVIDA);

        Template template = fmConfiguration.getTemplate("email-template-universal.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }

}