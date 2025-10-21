package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;

import java.util.List;

public record ItensAbrangentes(List<CompanyDomain> empresas, List<PlanDomain> planta) {

}
