package com.ocoelhogabriel.security_control_custom.application.dto;

import java.util.List;

import org.springframework.lang.NonNull;

public record CheckAbrangenciaRec(List<Long> listAbrangencia, @NonNull Integer isHier) {

}
