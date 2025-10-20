package com.ocoelhogabriel.security_control_custom.application.usecase;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaDTO;

public interface IEmpresaService {

	ResponseEntity<Void> empresaDeleteById(Long codigo) throws IOException;

	ResponseEntity<Page<EmpresaDTO>> empresaFindAllPaginado(String nome, Pageable pageable) throws IOException;

	ResponseEntity<List<EmpresaDTO>> empresaFindAll() throws IOException;

	ResponseEntity<EmpresaDTO> empresaUpdate(Long codigo, EmpresaModel empresaModel) throws IOException;

	ResponseEntity<EmpresaDTO> empresaSave(EmpresaModel empresaModel) throws IOException;

	ResponseEntity<EmpresaDTO> findByIdApi(Long codigo) throws IOException;

	ResponseEntity<EmpresaDTO> empresaFindByCnpjApi(Long codigo) throws IOException;

	List<EmpresaDTO> sendListAbrangenciaEmpresaDTO();

}
