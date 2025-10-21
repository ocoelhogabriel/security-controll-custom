package com.ocoelhogabriel.security_control_custom.application.port.in;

import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioModel;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioPermissaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Driving Port para os casos de uso relacionados a Usuários.
 * Define o contrato que a camada de aplicação oferece para o mundo exterior.
 */
public interface IUsuarioService {

    /**
     * Busca um usuário pelo seu ID.
     * @param codigo O ID do usuário.
     * @return O DTO do usuário encontrado.
     * @throws com.ocoelhogabriel.security_control_custom.application.exception.UserNotFoundException se o usuário não for encontrado.
     */
    UsuarioDTO findById(@NonNull Long codigo);

    /**
     * Busca todos os usuários.
     * @return Uma lista com os DTOs de todos os usuários.
     */
    List<UsuarioDTO> findAll();

    /**
     * Atualiza um usuário existente.
     * @param codigo O ID do usuário a ser atualizado.
     * @param userModel O modelo com os dados do usuário.
     * @return O DTO do usuário atualizado.
     */
    UsuarioDTO saveUpdateEncodePassword(@NonNull Long codigo, @NonNull UsuarioModel userModel);

    /**
     * Cria um novo usuário.
     * @param userModel O modelo com os dados do usuário.
     * @return O DTO do usuário criado.
     */
    UsuarioDTO saveUpdateEncodePassword(@NonNull UsuarioModel userModel);

    /**
     * Busca usuários de forma paginada.
     * @param nome Filtro opcional pelo nome do usuário.
     * @param pageable Informações de paginação.
     * @return Uma página de DTOs de usuários.
     */
    Page<UsuarioDTO> findAll(String nome, @NonNull Pageable pageable);

    /**
     * Deleta um usuário pelo seu ID.
     * @param codigo O ID do usuário a ser deletado.
     */
    void delete(@NonNull Long codigo);

    /**
     * Busca as permissões de um usuário pelo seu ID.
     * @param codigo O ID do usuário.
     * @return Um DTO com as permissões do usuário.
     */
    UsuarioPermissaoDTO findByIdPermission(@NonNull Long codigo);

}
