package com.ocoelhogabriel.security_control_custom.application.port.in;

import com.ocoelhogabriel.security_control_custom.application.dto.AuthModel;
import com.ocoelhogabriel.security_control_custom.application.dto.ResponseAuthDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.TokenValidationResponseDTO;

/**
 * Driving Port para os casos de uso relacionados a Autenticação.
 * Define o contrato para gerar e validar tokens de acesso, e para o processo de login.
 */
public interface IAuthenticationService {

    /**
     * Realiza o processo de autenticação (login) e gera um token JWT.
     *
     * @param authModel O modelo com os dados de login e senha.
     * @return Um DTO contendo o token gerado e informações do usuário.
     */
    ResponseAuthDTO authenticateAndGenerateToken(AuthModel authModel);

    /**
     * Gera um token de autenticação com base nas credenciais fornecidas (usado internamente ou para casos específicos).
     *
     * @param authModel O modelo com os dados de login e senha.
     * @return Uma string representando o token JWT gerado.
     */
    String generateToken(AuthModel authModel);

    /**
     * Valida um token de autenticação existente.
     *
     * @param token O token JWT a ser validado.
     * @return O login (subject) contido no token se ele for válido.
     */
    String validateToken(String token);

    /**
     * Atualiza um token JWT expirado ou próximo de expirar.
     *
     * @param token O token JWT a ser atualizado.
     * @return Um DTO contendo o novo token e informações atualizadas.
     */
    ResponseAuthDTO refreshToken(String token);

    /**
     * Valida e parseia um token, retornando informações sobre sua validade e expiração.
     *
     * @param token O token JWT a ser validado e parseado.
     * @return Um DTO com o status de validação e detalhes do token.
     */
    TokenValidationResponseDTO validateAndParseToken(String token);
}
