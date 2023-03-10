package com.br.dbc.captacao.dto.candidato;

import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoCreateDTO {

    @NotNull
    @Size(min = 3, max = 255, message = "O nome deve ter de 3 a 255 caracteres")
    @Schema(description = "Nome do candidato", example = "José da Silva da Silva")
    @Pattern(regexp = "^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]{0,}", message = "Não permitido números e caracteres especiais.")
    private String nome;

    @NotNull
    @Schema(description = "data de nascimento do candidato", example = "20/10/2000")
    private LocalDate dataNascimento;

    @Email
    @Schema(description = "Email do candidato", example = "email@mail.com")
    private String email;

    @NotNull
    @Size(min = 8, max = 255, message = "O nome deve ter de 8 a 30 caracteres")
    @Schema(description = "Telefone do candidato", example = "32251515")
    private String telefone;

    @NotNull
    @Size(min = 3, max = 255, message = "O nome deve ter de 8 a 30 caracteres")
    @Schema(description = "Registro Geral (RG)", example = "77.777.777-7")
    private String rg;

    @CPF
    @Schema(description = "Cadastro de pessoa fisica (CPF)", example = "777.777.777-77")
    private String cpf;

    @NotNull
    @Schema(description = "Estado em que habita o candidato", example = "RS")
    private String estado;

    @NotNull
    @Size(min = 3, max= 255, message = "O nome deve ter de 3 a 30 caracteres")
    @Schema(description = "Cidade em que habita o candidato", example = "Porto Alegre")
    private String cidade;

    @Schema(example = "Não possui", description = "Tipo de pcd")
    private String pcd;

    @JsonIgnore
    @Schema(description = "Observaçoes a respeito do candidato", example = "Boa Logica")
    private String observacoes;

    @JsonIgnore
    @Schema(description = "Nota comportamental da entrevista do candidato", example = "Pisca muito")
    private Double notaEntrevistaComportamental;

    @JsonIgnore
    @Schema(description = "Nota tecnica da entrevista do candidato", example = "Soube responder as perguntas feitas")
    private Double notaEntrevistaTecnica;

    @Schema(example = "T", description = "(T)TRUE or (F)FALSE")
    private TipoMarcacao ativo;

    @JsonIgnore
    @Schema(description = "Parecer comportamental do candidato", example = "Otimo comportamento")
    private String parecerComportamental;

    @JsonIgnore
    @Schema(description = "Parecer técnico do candidato", example = "Baixo nivel de raciocínio lógico")
    private String parecerTecnico;

    @JsonIgnore
    private Double media;

    @NotNull
    private List<String> linguagens;

    @Schema(example = "1ª Edição")
    private EdicaoDTO edicao;

    @NotNull
    private Integer formulario;


}
