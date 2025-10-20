package com.ocoelhogabriel.security_control_custom.infrastructure.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(JsonNode attribute) {
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error converting JsonNode to String", e);
		}
	}

	@Override
	public JsonNode convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readTree(dbData);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error converting String to JsonNode", e);
		}
	}
}
