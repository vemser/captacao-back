package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.login.LoginDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

public interface AuthControllerInterface {


    @Operation(summary = "Confirmar uma entrevista agendada.", description = "Confirma uma entrevista agendada.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Entrevista confirmada."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping
    void confirmarEntrevista(@PathVariable @Valid String tokenEntrevista) throws RegraDeNegocioException;
}
