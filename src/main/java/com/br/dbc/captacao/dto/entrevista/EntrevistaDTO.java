package com.br.dbc.captacao.dto.entrevista;

import com.br.dbc.captacao.enums.Legenda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrevistaDTO extends EntrevistaCreateDTO {

    private Integer idEntrevista;

//    private CandidatoDTO candidatoDTO;
//
//    private UsuarioDTO usuarioDTO;

    private Legenda legenda;
}
