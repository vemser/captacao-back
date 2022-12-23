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

    @NotNull
    @Schema(example = "true", description = "T(true) or F(false)")
    private boolean matriculadoBoolean;

    @NotNull
    @Schema(example = "Analise e Desenvolvimento de Software", description = "Nome do curso")
    private String curso;

    @NotNull
    @Schema(example = "NOITE", description = "Turno que estuda: MANHA, TARDE ou NOITE")
    private TipoTurno turno;

    @NotNull
    @Schema(example = "PUC", description = "Nome da Instituição onde cursa")
    private String instituicao;

    @Schema(example = "https://github.com/link-github", description = "Link referente ao seu Github")
    private String github;

    @Schema(example = "https://linkedin.com/", description = "Link referente ao seu Linkedin")
    private String linkedin;

    @NotNull
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean desafiosBoolean;

    @NotNull
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean problemaBoolean;

    @NotNull
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean reconhecimentoBoolean;

    @NotNull
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean altruismoBoolean;


    @Schema(example = "Outro", description = "Motivo pelo qual se interessou pela área de Tecnologia")
    private String resposta;

    @NotNull
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean lgpdBoolean;

    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean provaBoolean;

    @NotNull
    @Schema(example = "Não possuo", description = "Selecione seu nivel de inglês")
    private String ingles;

    @NotNull
    @Schema(example = "Não possuo", description = "Selecione seu nivel de espanhol")
    private String espanhol;


    @Schema(example = "TDAH", description = "Digite sua neurodiversidade")
    private String neurodiversidade;

    @NotNull
    @Schema(example = "16 Gb de RAM", description = "Configurações do computador")
    private String configuracoes;

    @NotNull
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean efetivacaoBoolean;

    @NotNull
    @Schema(example = "true", description = "T(TRUE) or F(FALSE)")
    private boolean disponibilidadeBoolean;

    @NotNull
    @Schema(example = "Mulher Cis")
    private String genero;

    @NotNull
    @Schema(example = "Heterosexual")
    private String orientacao;


    @Schema(example = "[ 1 ]", description = "Escolha sua(s) trilha(s) entre Frontend (1), Backend (2) e QA (3)")
    private List<Integer> trilhas;

    @NotNull
    @Schema(example = "importancia")
    private String importancia;
}
