package com.br.dbc.captacao.dto.formulario;

import com.br.dbc.captacao.enums.TipoTurno;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormularioCreateDTO {
    @Schema(example = "true", description = "T(true) or F(false)")
    private boolean matriculadoBoolean;

    @NotBlank(message = "O campo Curso não deve ser vazio ou nulo.")
    @Schema(example = "Analise e Desenvolvimento de Software", description = "Nome do curso")
    private String curso;

    @NotNull(message = "O campo Turno não deve ser nulo.")
    @Schema(example = "NOITE", description = "Turno que estuda: MANHA, TARDE ou NOITE")
    private TipoTurno turno;

    @NotBlank(message = "O campo Instituição não deve ser vazio ou nulo.")
    @Schema(example = "PUC", description = "Nome da Instituição onde cursa")
    private String instituicao;

    @NotEmpty(message = "O campo Github não deve ser vazio.")
    @Schema(example = "https://github.com/link-github", description = "Link referente ao seu Github")
    private String github;

    @NotEmpty(message = "O campo Linkedin não deve ser vazio.")
    @Schema(example = "https://linkedin.com/", description = "Link referente ao seu Linkedin")
    private String linkedin;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean desafiosBoolean;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean problemaBoolean;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean reconhecimentoBoolean;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean altruismoBoolean;

    @NotEmpty(message = "O campo Resposta não deve ser vazio.")
    @Schema(example = "Outro", description = "Motivo pelo qual se interessou pela área de Tecnologia")
    private String resposta;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean lgpdBoolean;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean provaBoolean;

    @NotBlank(message = "O campo nível de Inglês não deve ser vazio ou nulo.")
    @Schema(example = "Não possuo", description = "Selecione seu nivel de inglês")
    private String ingles;

    @NotBlank(message = "O campo nível de Espanhol não deve ser vazio ou nulo.")
    @Schema(example = "Não possuo", description = "Selecione seu nivel de espanhol")
    private String espanhol;

    @NotEmpty(message = "O campo Neurodiversidade não deve ser vazio.")
    @Schema(example = "TDAH", description = "Digite sua neurodiversidade")
    private String neurodiversidade;

    @NotBlank(message = "O campo Configuração do computador não deve ser vazio ou nulo.")
    @Schema(example = "16 Gb de RAM", description = "Configurações do computador")
    private String configuracoes;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean efetivacaoBoolean;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean disponibilidadeBoolean;

    @NotBlank(message = "O campo Gênero não deve ser vazio ou nulo.")
    @Schema(example = "Mulher Cis")
    private String genero;

    @NotBlank(message = "O campo Orientação sexual não deve ser vazio ou nulo.")
    @Schema(example = "Heterosexual")
    private String orientacao;

    @Schema(example = "[ 1 ]", description = "Escolha sua(s) trilha(s) entre Frontend (1), Backend (2) e QA (3)")
    private List<Integer> trilhas;

    @NotBlank(message = "O campo Importância não deve ser vazio ou nulo.")
    @Schema(example = "importancia")
    private String importancia;
}
