package com.br.dbc.captacao.dto;

import lombok.Data;

@Data
public class SendEmailDTO {

    private String email;
    private String nome;
    private String urlToken;

    private String data;

}