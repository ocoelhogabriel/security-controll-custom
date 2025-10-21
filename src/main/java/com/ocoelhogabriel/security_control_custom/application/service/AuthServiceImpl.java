package com.ocoelhogabriel.security_control_custom.application.service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ocoelhogabriel.security_control_custom.application.dto.*;
import com.ocoelhogabriel.security_control_custom.application.exception.AuthenticationFailedException;
import com.ocoelhogabriel.security_control_custom.application.exception.InvalidTokenException;
import com.ocoelhogabriel.security_control_custom.application.exception.UserNotFoundException;
import com.ocoelhogabriel.security_control_custom.application.port.in.IAuthenticationService;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserAuthDetails;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.security.GenerateTokenRecords;
import com.ocoelhogabriel.security_control_custom.infrastructure.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
public class AuthServiceImpl implements IAuthenticationService, UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UsuarioServiceImpl userService;

    @Autowired
    @Lazy
    protected AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDomain userDomain = userService.findLogin(username);
            return new UserAuthDetails(userDomain);
        } catch (UserNotFoundException e) {
            log.info("Tentativa de login para usuário não encontrado: {}", username);
            throw new UsernameNotFoundException("Usuário não encontrado: " + username, e);
        }
    }

    @Override
    public ResponseAuthDTO authenticateAndGenerateToken(@NonNull AuthModel authReq) {
        Objects.requireNonNull(authReq.getLogin(), "Login está nulo.");
        Objects.requireNonNull(authReq.getSenha(), "Senha está nula.");

        try {
            var userAuthenticationToken = new UsernamePasswordAuthenticationToken(authReq.getLogin(), authReq.getSenha());
            authenticationManager.authenticate(userAuthenticationToken);

            UserDomain userCheck = userService.findLogin(authReq.getLogin());
            GenerateTokenRecords tokenGenerate = jwtUtil.generateToken(userCheck);

            return new ResponseAuthDTO(tokenGenerate.token(), tokenGenerate.date(), tokenGenerate.expiryIn(), userCheck.getId(), new PerfilDTO(userCheck.getProfileDomain()));
        } catch (AuthenticationException e) {
            log.warn("Falha na autenticação para o usuário: {}", authReq.getLogin());
            throw new AuthenticationFailedException("Autenticação falhou. Verifique o login e a senha.", e);
        }
    }

    @Override
    public String generateToken(AuthModel authModel) {
        Objects.requireNonNull(authModel.getLogin(), "Login está nulo.");
        Objects.requireNonNull(authModel.getSenha(), "Senha está nula.");

        UserDomain userDomain = userService.findLogin(authModel.getLogin());
        return jwtUtil.generateToken(userDomain).token();
    }

    @Override
    public String validateToken(String token) {
        try {
            return jwtUtil.validateToken(token);
        } catch (TokenExpiredException e) {
            throw new InvalidTokenException("Token expirado.", e);
        } catch (JWTVerificationException | JWTCreationException e) {
            log.error("Erro ao validar o token: {}", e.getMessage());
            throw new InvalidTokenException("Token inválido.", e);
        }
    }

    @Override
    public ResponseAuthDTO refreshToken(String token) {
        try {
            var refresh = jwtUtil.validateOrRefreshToken(token);
            UserDomain userCheck = userService.findLogin(refresh.username());
            return new ResponseAuthDTO(refresh.token(), refresh.date(), refresh.expiryIn(), userCheck.getId(), new PerfilDTO(userCheck.getProfileDomain()));
        } catch (JWTVerificationException e) {
            log.error("Erro ao atualizar o token: {}", e.getMessage());
            throw new InvalidTokenException("Erro ao atualizar o token.", e);
        }
    }

    @Override
    public TokenValidationResponseDTO validateAndParseToken(@NonNull String token) {
        Objects.requireNonNull(token, "Token está nulo.");

        String username = validateToken(token); // This already handles exceptions
        if (username == null) {
            // This case should ideally not be reached if validateToken throws exceptions
            throw new InvalidTokenException("Token inválido.");
        }

        try {
            Instant expiration = jwtUtil.getExpirationDateFromToken(token);
            long timeToExpiry = Duration.between(Instant.now(), expiration).toMillis();
            LocalDateTime expirationLocalDateTime = expiration.atZone(ZoneId.systemDefault()).toLocalDateTime();

            return new TokenValidationResponseDTO(true, timeToExpiry, expirationLocalDateTime.toString());
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Erro ao parsear o tempo de expiração do token.", e);
        }
    }
}
