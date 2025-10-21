package com.ocoelhogabriel.security_control_custom.application.port.in;

import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaListaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Driving Port para os casos de uso relacionados a Abrangência (Scope).
 */
public interface IAbrangenciaService {

    /**
     * Atualiza uma abrangência existente.
     * @param codigo O ID da abrangência a ser atualizada.
     * @param abrangenciaModel O modelo com os novos dados.
     * @return O DTO da abrangência atualizada.
     */
    AbrangenciaListaDetalhesDTO update(@NonNull Long codigo, AbrangenciaModel abrangenciaModel);

    /**
     * Cria uma nova abrangência.
     * @param abrangenciaModel O modelo com os dados da abrangência.
     * @return O DTO da abrangência criada.
     */
    AbrangenciaListaDetalhesDTO save(AbrangenciaModel abrangenciaModel);

    /**
     * Busca todas as abrangências.
     * @return Uma lista com os DTOs de todas as abrangências.
     */
    List<AbrangenciaListaDetalhesDTO> findAll();

    /**
     * Busca uma abrangência pelo seu ID.
     * @param codigo O ID da abrangência.
     * @return O DTO da abrangência encontrada.
     */
    AbrangenciaListaDetalhesDTO findById(@NonNull Long codigo);

    /**
     * Deleta uma abrangência pelo seu ID.
     * @param codigo O ID da abrangência a ser deletada.
     */
    void delete(@NonNull Long codigo);

    /**
     * Busca abrangências de forma paginada.
     * @param nome Filtro opcional pelo nome da abrangência.
     * @param pageable Informações de paginação.
     * @return Uma página de DTOs de abrangências.
     */
    Page<AbrangenciaListaDetalhesDTO> findAll(String nome, Pageable pageable);

}
