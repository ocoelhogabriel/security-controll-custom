package com.ocoelhogabriel.security_control_custom.application.port.in;

import com.ocoelhogabriel.security_control_custom.application.dto.PerfilModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PerfilPermissaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Driving Port para os casos de uso relacionados a Perfis e Permissões.
 */
public interface IPerfilPermService {

    /**
     * Cria um novo perfil com suas permissões.
     * @param perModel O modelo com os dados do perfil.
     * @return O DTO do perfil criado.
     */
    PerfilPermissaoDTO save(PerfilModel perModel);

    /**
     * Atualiza um perfil existente.
     * @param codigo O ID do perfil a ser atualizado.
     * @param perModel O modelo com os novos dados do perfil.
     * @return O DTO do perfil atualizado.
     */
    PerfilPermissaoDTO update(@NonNull Long codigo, PerfilModel perModel);

    /**
     * Busca todos os perfis.
     * @return Uma lista com os DTOs de todos os perfis.
     */
    List<PerfilPermissaoDTO> findAll();

    /**
     * Busca um perfil pelo seu ID.
     * @param codigo O ID do perfil.
     * @return O DTO do perfil encontrado.
     */
    PerfilPermissaoDTO findById(@NonNull Long codigo);

    /**
     * Deleta um perfil pelo seu ID.
     * @param codigo O ID do perfil a ser deletado.
     */
    void delete(@NonNull Long codigo);

    /**
     * Busca perfis de forma paginada.
     * @param nome Filtro opcional pelo nome do perfil.
     * @param pageable Informações de paginação.
     * @return Uma página de DTOs de perfis.
     */
    Page<PerfilPermissaoDTO> findAll(String nome, @NonNull Pageable pageable);

}
