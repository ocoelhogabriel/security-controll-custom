package com.ocoelhogabriel.security_control_custom.application.dto;

public class TokenValidationResponseDTO {

	public boolean tokenIsValid;
	public long timeToExpiryInMillis;
	public String tokenToExpiryInDateTime;

	public boolean getIsTokenIsValid() {
		return tokenIsValid;
	}

	public void setTokenIsValid(boolean tokenIsValid) {
		this.tokenIsValid = tokenIsValid;
	}

	public long getTimeToExpiryInMillis() {
		return timeToExpiryInMillis;
	}

	public void setTimeToExpiryInMillis(long timeToExpiryInMillis) {
		this.timeToExpiryInMillis = timeToExpiryInMillis;
	}

	public String getTokenToExpiryInDateTime() {
		return tokenToExpiryInDateTime;
	}

	public void setTokenToExpiryInDateTime(String tokenToExpiryInDateTime) {
		this.tokenToExpiryInDateTime = tokenToExpiryInDateTime;
	}

	public TokenValidationResponseDTO(boolean tokenIsValid, long timeToExpiryInMillis, String tokenToExpiryInDateTime) {
		super();
		this.tokenIsValid = tokenIsValid;
		this.timeToExpiryInMillis = timeToExpiryInMillis;
		this.tokenToExpiryInDateTime = tokenToExpiryInDateTime;
	}

	public TokenValidationResponseDTO() {
		super();

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TokenValidationResponseDTO [tokenIsValid=").append(tokenIsValid).append(", timeToExpiryInMillis=").append(timeToExpiryInMillis).append(", ");
		if (tokenToExpiryInDateTime != null) {
			builder.append("tokenToExpiryInDateTime=").append(tokenToExpiryInDateTime);
		}
		builder.append("]");
		return builder.toString();
	}

}
