package com.br.dbc.captacao.dto.formulario;

import com.br.dbc.captacao.enums.TipoTurno;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormularioCreateDTO {
    @NotNull(message = "O campo Matrícula não deve ser nulo.")
    @Schema(example = "true", description = "T(true) or F(false)")
    private boolean matriculadoBoolean;

    @NotNull(message = "O campo Curso não deve ser nulo.")
    @Schema(example = "Analise e Desenvolvimento de Software", description = "Nome do curso")
    private String curso;

    @NotNull(message = "O campo Turno não deve ser nulo.")
    @Schema(example = "NOITE", description = "Turno que estuda: MANHA, TARDE ou NOITE")
    private TipoTurno turno;

    @NotNull(message = "O campo Instituição não deve ser nulo.")
    @Schema(example = "PUC", description = "Nome da Instituição onde cursa")
    private String instituicao;

    @Schema(example = "https://github.com/link-github", description = "Link referente ao seu Github")
    private String github;

    @Schema(example = "https://linkedin.com/", description = "Link referente ao seu Linkedin")
    private String linkedin;

    @NotNull(message = "O campo Desafio não deve ser nulo.")
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean desafiosBoolean;

    @NotNull(message = "O campo Problema não deve ser nulo.")
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean problemaBoolean;

    @NotNull(message = "O campo Reconhecimento não deve ser nulo.")
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean reconhecimentoBoolean;

    @NotNull(message = "O campo Altruismo não deve ser nulo.")
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean altruismoBoolean;

    @Schema(example = "Outro", description = "Motivo pelo qual se interessou pela área de Tecnologia")
    private String resposta;

    @NotNull(message = "O campo LGPD não deve ser nulo.")
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean lgpdBoolean;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean provaBoolean;

    @NotNull(message = "O campo nível de Inglês não deve ser nulo.")
    @Schema(example = "Não possuo", description = "Selecione seu nivel de inglês")
    private String ingles;

    @NotNull(message = "O campo nível de Espanhol não deve ser nulo.")
    @Schema(example = "Não possuo", description = "Selecione seu nivel de espanhol")
    private String espanhol;

    @Schema(example = "TDAH", description = "Digite sua neurodiversidade")
    private String neurodiversidade;

    @NotNull(message = "O campo Configuração do computador não deve ser nulo.")
    @Schema(example = "16 Gb de RAM", description = "Configurações do computador")
    private String configuracoes;

    @NotNull(message = "O campo Efetivação não deve ser nulo.")
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean efetivacaoBoolean;

    @NotNull(message = "O campo Disponibilidade não deve ser nulo.")
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean disponibilidadeBoolean;

    @NotNull(message = "O campo Gênero não deve ser nulo.")
    @Schema(example = "Mulher Cis")
    private String genero;

    @NotNull(message = "O campo Orientação sexual não deve ser nulo.")
    @Schema(example = "Heterosexual")
    private String orientacao;

    @Schema(example = "[ 1 ]", description = "Escolha sua(s) trilha(s) entre Frontend (1), Backend (2) e QA (3)")
    private List<Integer> trilhas;

    @NotNull(message = "O campo Importância não deve ser nulo.")
    @Schema(example = "importancia")
    private String importancia;
}
