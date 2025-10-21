package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    private final CompanyMapper companyMapper;

    public PlanMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    public PlanDomain toDomain(Plan entity) {
        if (entity == null) {
            return null;
        }
        CompanyDomain companyDomain = companyMapper.toDomain(entity.getEmpresa());
        return new PlanDomain(entity.getId(), entity.getName(), companyDomain);
    }

    public Plan toEntity(PlanDomain domain) {
        if (domain == null) {
            return null;
        }
        Company company = companyMapper.toEntity(domain.getCompanyDomain());
        return new Plan(domain.getId(), domain.getName(), company);
    }
}
