package com.br.dbc.captacao.dto.login;

import com.br.dbc.captacao.dto.CargoDTO;
import lombok.Data;

import java.util.List;

@Data
public class LoginRetornoDTO {
    private String email;
    private String nomeCompleto;
    private List<CargoDTO> perfis;
}