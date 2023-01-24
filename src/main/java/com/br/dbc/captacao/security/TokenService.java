package com.br.dbc.captacao.security;

import com.br.dbc.captacao.entity.CargoEntity;
import com.br.dbc.captacao.entity.EntrevistaEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String CHAVE_CARGOS = "cargos";
    private static final String CHAVE_LOGIN = "login";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.expiration}")
    private String expirationChangePassword;

    public String getToken(GestorEntity usuarioEntity, String expiration) {
        if (expiration != null) {
            this.expiration = expiration;
        }
        LocalDateTime localDateTimeAtual = LocalDateTime.now();
        Date dataAtual = Date.from(localDateTimeAtual.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime dateExpiracaoLocalDate = localDateTimeAtual.plusMonths(Long.parseLong(this.expiration));
        Date expiracao = Date.from(dateExpiracaoLocalDate.atZone(ZoneId.systemDefault()).toInstant());


        List<String> cargosDoUsuario = usuarioEntity.getCargoEntity().stream()
                .map(CargoEntity::getAuthority)
                .toList();

        return Jwts.builder()
                .claim(CHAVE_LOGIN, usuarioEntity.getEmail())
                .claim(Claims.ID, usuarioEntity.getIdGestor())
                .claim(CHAVE_CARGOS, cargosDoUsuario)
                .setIssuedAt(dataAtual)
                .setExpiration(expiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token == null) {
            return null;
        }

        token = token.replace("Bearer ", "");

        Claims chaves = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        String idUsuario = chaves.get(Claims.ID, String.class);

        List<String> cargos = chaves.get(CHAVE_CARGOS, List.class);

        List<SimpleGrantedAuthority> cargosList = cargos.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();


        return new UsernamePasswordAuthenticationToken(idUsuario,
                null, cargosList);
    }

    public String getEmailByToken(String token) throws InvalidTokenException {
        token = token.replace("Bearer ", "");
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(Claims.ID, String.class);
        } catch (ExpiredJwtException exception) {
            throw new InvalidTokenException("Token Expirado");
        }
    }

    public String getTokenConfirmacao(EntrevistaEntity entrevistaEntity){
        LocalDateTime dataAtual = LocalDateTime.now();
        Date now = Date.from(dataAtual.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime dataExpiracao = dataAtual.plusDays(Long.parseLong(expirationChangePassword));
        Date exp = Date.from(dataExpiracao.atZone(ZoneId.systemDefault()).toInstant());

        String meuToken = Jwts.builder()
                .setIssuer("facetoface-api")
                .claim(Claims.ID, entrevistaEntity.getCandidatoEntity().getEmail())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return meuToken;
    }
}
