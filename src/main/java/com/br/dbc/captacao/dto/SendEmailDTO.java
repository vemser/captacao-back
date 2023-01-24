package com.br.dbc.captacao.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SendEmailDTO {

    private String email;
    private String nome;
    private String urlToken;

    private String data;

}