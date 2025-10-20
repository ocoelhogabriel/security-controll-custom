package com.ocoelhogabriel.security_control_custom.application.dto;

public class TokenValidationDeviceDTO {

	public boolean tokenIsValid;
	public String tokenDateTime;
	public String tokenToExpiryInDateTime;

	public boolean getIsTokenIsValid() {
		return tokenIsValid;
	}

	public void setTokenIsValid(boolean tokenIsValid) {
		this.tokenIsValid = tokenIsValid;
	}

	public String getTokenDateTime() {
		return tokenDateTime;
	}

	public void setTokenDateTime(String tokenDateTime) {
		this.tokenDateTime = tokenDateTime;
	}

	public String getTokenToExpiryInDateTime() {
		return tokenToExpiryInDateTime;
	}

	public void setTokenToExpiryInDateTime(String tokenToExpiryInDateTime) {
		this.tokenToExpiryInDateTime = tokenToExpiryInDateTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TokenValidationDeviceDTO [tokenIsValid=").append(tokenIsValid).append(", ");
		if (tokenDateTime != null) {
			builder.append("tokenDateTime=").append(tokenDateTime).append(", ");
		}
		if (tokenToExpiryInDateTime != null) {
			builder.append("tokenToExpiryInDateTime=").append(tokenToExpiryInDateTime);
		}
		builder.append("]");
		return builder.toString();
	}

	public TokenValidationDeviceDTO(boolean tokenIsValid, String tokenDateTime, String tokenToExpiryInDateTime) {
		super();
		this.tokenIsValid = tokenIsValid;
		this.tokenDateTime = tokenDateTime;
		this.tokenToExpiryInDateTime = tokenToExpiryInDateTime;
	}

	public TokenValidationDeviceDTO() {
		super();

	}

}
