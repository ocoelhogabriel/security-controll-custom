package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.security_control_custom.application.handler.AbrangenciaHandler;
import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaDTO;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;
import com.ocoelhogabriel.security_control_custom.application.dto.CheckAbrangenciaRec;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.CompanyJpaRepository;
import com.ocoelhogabriel.security_control_custom.application.usecase.IEmpresaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EmpresaServiceImpl implements IEmpresaService {

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

	@Autowired
	private CompanyJpaRepository companyJpaRepository;

	@Autowired
	private AbrangenciaHandler abrangenciaHandler;

	private static final String EMPRESA = "EMPRESA";

	private CheckAbrangenciaRec findAbrangencia() {
		return abrangenciaHandler.checkAbrangencia(EMPRESA);
	}

	@Override
	public ResponseEntity<Void> empresaDeleteById(Long codigo) throws IOException {

		try {
			var empresa = findByIdEntity(codigo);
			if (empresa == null)
				throw new EntityNotFoundException("Empresa não encontrada com o código: " + codigo);

			companyJpaRepository.deleteById(empresa.getId());
			return MessageResponse.noContent();
		} catch (EmptyResultDataAccessException e) {
			log.error("Erro ao deletar a empresa. Erro: ", e);
			throw new EntityNotFoundException("Empresa não encontrada com o código: " + codigo, e);
		}
	}

	@Override
	public ResponseEntity<Page<EmpresaDTO>> empresaFindAllPaginado(String nome, Pageable pageable) throws IOException {
		Objects.requireNonNull(pageable, "Pageable da Empresa está nulo.");

		Specification<Company> spec = Specification.where(null);

		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(Company.filterByFields(nome, null));
		} else {
			spec = spec.and(Company.filterByFields(nome, findAbrangencia().listAbrangencia()));
		}

		Page<Company> result = companyJpaRepository.findAll(spec, pageable);
		return MessageResponse.success(result.map(EmpresaDTO::new));
	}

	@Override
	public List<EmpresaDTO> sendListAbrangenciaEmpresaDTO() {
		return companyJpaRepository.findAll().stream().map(EmpresaDTO::new).toList();
	}

	@Override
	public ResponseEntity<List<EmpresaDTO>> empresaFindAll() throws IOException {

		Specification<Company> spec = Specification.where(null);

		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(Company.filterByFields(null, null));
		} else {
			spec = spec.and(Company.filterByFields(null, findAbrangencia().listAbrangencia()));
		}
		List<Company> result = companyJpaRepository.findAll(spec);
		return MessageResponse.success(result.stream().map(EmpresaDTO::new).toList());
	}

	@Override
	public ResponseEntity<EmpresaDTO> empresaUpdate(Long codigo, EmpresaModel empresaModel) throws IOException {
		Objects.requireNonNull(empresaModel.getCnpj(), "CNPJ da Empresa está nulo.");
		Objects.requireNonNull(empresaModel.getNome(), "Nome da Empresa está nulo.");

		var empresa = findByIdEntity(codigo);

		String nomeFantasia = Optional.ofNullable(empresaModel.getNomeFantasia()).orElse(empresa.getTradeName());
		String telefone = Optional.ofNullable(empresaModel.getTelefone()).orElse(empresa.getContact());

		empresa.setDocument(empresaModel.getCnpj());
		empresa.setName(empresaModel.getNome());
		empresa.setTradeName(nomeFantasia);
		empresa.setContact(telefone);

		return MessageResponse.success(new EmpresaDTO(companyJpaRepository.save(empresa)));
	}

	@Override
	public ResponseEntity<EmpresaDTO> empresaSave(EmpresaModel empresaModel) throws IOException {
		Objects.requireNonNull(empresaModel.getCnpj(), "CNPJ da Empresa está nulo.");
		Objects.requireNonNull(empresaModel.getNome(), "Nome da Empresa está nulo.");

		try {
			Company company = new Company(null, empresaModel.getCnpj(), empresaModel.getNome(), empresaModel.getNomeFantasia(), empresaModel.getTelefone());
			Company savedCompany = companyJpaRepository.save(company);
			return MessageResponse.create(new EmpresaDTO(savedCompany));
		} catch (Exception e) {
			log.error("Erro ao realizar o cadastro de uma empresa.", e);
			throw new IOException("Erro ao realizar o cadastro de uma empresa.", e);
		}
	}

	@Override
	public ResponseEntity<EmpresaDTO> findByIdApi(Long codigo) throws IOException {


		var empresa = companyJpaRepository.findById(codigo).orElse(null);
		if (empresa == null) {
			throw new EntityNotFoundException("Empresa não encontrada.");
		}

		Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), empresa.getId());
		if (idPermitted == null) {
			throw new EntityNotFoundException("Sem Abrangência para essa empresa.");
		}
		return MessageResponse.success(new EmpresaDTO(empresa));
	}

	@Override
	public ResponseEntity<EmpresaDTO> empresaFindByCnpjApi(Long codigo) throws IOException {
		Company company = empresaFindByCnpjEntity(codigo);
		if (company == null) {
			throw new EntityNotFoundException("Empresa não encontrada.");
		}

		Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), company.getId());
		if (idPermitted == null) {
			throw new EntityNotFoundException("Sem Abrangência para essa empresa.");
		}

		return MessageResponse.success(new EmpresaDTO(company));
	}

	public Company findById(Long codigo) {
		Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
		Company emp = companyJpaRepository.findById(codigo).orElse(null);
		if (emp == null) {
			return null;
		}
		return findByIdAbrangencia(emp);
	}

	public Company findByIdAbrangencia(Company emp) {

		var findIdAbrangencia = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), emp.getId());
		if (findIdAbrangencia == null) {
			return null;
		}
		return emp;
	}

	public Company empresaFindByCnpjEntity(Long codigo) {
		Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
		return companyJpaRepository.findByEmpcnp(codigo).orElse(null);
	}

	public Company findByIdEntity(@NonNull Long codigo) {
		Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
		return companyJpaRepository.findById(codigo).orElse(null);
	}

}
