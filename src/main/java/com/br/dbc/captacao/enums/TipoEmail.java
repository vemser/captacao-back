package com.br.dbc.captacao.enums;

import lombok.Getter;

@Getter
public enum TipoEmail {


    INSCRICAO(" Tua candidatura para o processo seletivo foi recebida com sucesso. " +
            "  Avaliaremos o teu currículo e as respostas do questionário e," +
            " caso aprovado para a próxima etapa, entraremos em contato para dar seguimento no processo seletivo. <br>" +
            "As próximas etapas consistem na realização do teste de lógica e entrevista individual.", "DBC Company - Inscrição"),

    CONFIRMAR_ENTREVISTA(" Tua entrevista foi marcada com sucesso. " +
            "Para o dia: "
            , "DBC Company - Entrevista"),

    APROVADO("Olá, tudo bem? <br>" +
            "Gostaríamos de informar que foste aprovado(a) para a primeira <br>" +
            "etapa de seleção do Vem Ser DBC. <br>", "DBC Company - Primeira Etapa"),

    REPROVADO("Olá, tudo bem? <br>" +
            "Obrigado por ter participado do processo de seleção do Vem Ser DBC!" +
            "Neste momento, selecionamos apenas algumas pessoas dos milhares inscritos." +
            "Queremos que você continue tentando, pois este é apenas o começo," +
            "então separamos algumas dicas e links para que você siga evoluindo na área e venha trabalhar conosco numa próxima oportunidade.",
            "DBC Company - Primeira etapa."),

    RECOVER_PASSWORD("Problemas ao logar?<br>" +
            "Resetar sua senha é simples.<br>" +
            "Apenas pressione o botão abaixo e siga as instruções.<br>" +
            "Lembrando que o link expira em 5 minutos.<br>" +
            "<br><button style='display: block; background: #0078FF; border: none; border-radius: 7px; padding: 4px 2px;'><a href='/url/' style='color:white; padding:4px 8px'> Reset Password </a></button><br>" +
            " Se você não solicitou esta recuperação, apenas ignore este Email.<br>", "DBC Company - recuperação de senha");
    private String descricao;
    private String subject;

    TipoEmail(String descricao, String subject) {
        this.subject = subject;
        this.descricao = descricao;
    }

}
