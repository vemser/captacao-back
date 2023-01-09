package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.login.LoginDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.CandidatoFactory;
import com.br.dbc.captacao.factory.EntrevistaFactory;
import com.br.dbc.captacao.factory.GestorFactory;
import com.br.dbc.captacao.repository.EntrevistaRepository;
import com.br.dbc.captacao.security.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private CandidatoService candidatoService;
    @Mock
    private EntrevistaService entrevistaService;
    @Mock
    private EntrevistaRepository entrevistaRepository;
    @Mock
    private GestorService gestorService;

    @Test
    public void deveTestarAuthComSucesso() {
        //Setup
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("julio.gabriel@dbccompany.com.br");
        loginDTO.setSenha("123");

        Authentication authentication = new Authentication() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return GestorFactory.getGestorEntity();
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }
        };

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        //Act
        GestorEntity gestorEntity = authService.auth(loginDTO);

        //Assert
        assertNotNull(gestorEntity);
        assertEquals(gestorEntity.getEmail(),"julio.gabriel@dbccompany.com.br");
    }

    @Test
    public void deveTestarConfirmarEntrevistaComSucesso() throws RegraDeNegocioException {
        //Setup
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imd1c3Rhdm8ubHVjZW5hIiwianRpIjoiMTE0IiwiY2FyZ29zIjpbIlJPTEVfR0VTVEFPX0RFX1BFU1NPQVMiLCJST0xFX0FETUlOIiwiUk9MRV9JTlNUUlVUT1IiXSwiaWF0IjoxNjczMjgzNzQyLCJleHAiOjE2NzU4NzU3NDJ9.28Oi3mPCSq-zmmdWLwDEP2mz2-wwhYCAujuDPrnNwmU";
        String email = "julio.gabriel@dbccompany.com.br";

        when(tokenService.getEmailByToken(anyString())).thenReturn(email);
        when(candidatoService.findByEmailEntity(anyString())).thenReturn(CandidatoFactory.getCandidatoEntity());
        when(entrevistaService.findByCandidatoEntity(any(CandidatoEntity.class))).thenReturn(EntrevistaFactory.getEntrevistaEntity());

        //Act
        authService.confirmarEntrevista(token);

        //Assert
        verify(entrevistaRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarProcurarUsuarioComSucesso() throws RegraDeNegocioException {
        //Setup
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imd1c3Rhdm8ubHVjZW5hIiwianRpIjoiMTE0IiwiY2FyZ29zIjpbIlJPTEVfR0VTVEFPX0RFX1BFU1NPQVMiLCJST0xFX0FETUlOIiwiUk9MRV9JTlNUUlVUT1IiXSwiaWF0IjoxNjczMjgzNzQyLCJleHAiOjE2NzU4NzU3NDJ9.28Oi3mPCSq-zmmdWLwDEP2mz2-wwhYCAujuDPrnNwmU";
        String email = "julio.gabriel@dbccompany.com.br";

        when(tokenService.getEmailByToken(anyString())).thenReturn(email);
        when(gestorService.findByEmail(anyString())).thenReturn(GestorFactory.getGestorEntity());

        //Act
        String confirmEmail = authService.procurarUsuario(token);

        //Assert

        assertNotNull(confirmEmail);
        assertEquals(confirmEmail, email);
    }

}
