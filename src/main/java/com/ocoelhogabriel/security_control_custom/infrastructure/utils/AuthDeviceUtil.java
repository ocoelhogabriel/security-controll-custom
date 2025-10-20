package com.ocoelhogabriel.security_control_custom.infrastructure.utils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.security_control_custom.application.dto.TokenValidationDeviceDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AuthDeviceRecords;

@Component
public class AuthDeviceUtil {

	private static final DateTimeFormatter dtfPadrao = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static final String SECRET = "sirene";
	private static final long TIME = 1440; // 24 hours in minutes

	public static String gerarKeyBase() {
		return Utils.encode(SECRET);
	}

	public static boolean validarKeyBase(String token) {
		String tokenDecoded = Utils.decode(token);
		return tokenDecoded.equals(SECRET);
	}

	public static boolean validarTokenBase(String token) {
		String decodedToken = Utils.decode(token);
//		System.out.println("Decoded Token: " + decodedToken);
		String[] parse = splitSenha(decodedToken, "_");
		if (parse.length < 3)
			return false;

		// String numeroSerie = parse[0];
		String secret = parse[1];
		String dateString = parse[2];

		if (!validarKeyBase(secret))
			return false;

		LocalDateTime tokenTime = LocalDateTime.parse(dateString, dtfPadrao);
		return !isTokenExpired(tokenTime, TIME);
	}

	public static String convertDateToString() {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
		return dtfPadrao.format(localDateTime);
	}

	public static String[] splitSenha(String senha, String typeSplit) {
		try {
			return senha.split(typeSplit);
		} catch (RuntimeException e) {
			return new String[0];
		}
	}

	public static boolean isValidDate(String dateString) throws IOException {
		try {
			LocalDateTime.parse(dateString, dtfPadrao);
			return true;
		} catch (Exception e) {
			throw new IOException("Data não é compatível com o padrão esperado.");
		}
	}

	public static String gerarTokenBase(String senha) throws IOException {
		try {
			String[] parse = splitSenha(senha, "@");
			if (parse == null || parse.length < 2 || !isValidDate(parse[1])) {
				throw new IllegalArgumentException("Senha não é compatível com o modelo esperado.");
			}
			String token = parse[0] + "_" + gerarKeyBase() + "_" + parse[1];
			return Utils.encode(token);
		} catch (RuntimeException e) {
			return null;
		}
	}

	public static AuthDeviceRecords validarTokenBaseReturn(String token) {
		try {

			String decodedToken = Utils.decode(token);
			System.out.println("Decoded Token: " + decodedToken);
			String[] parse = splitSenha(decodedToken, "_");
			if (parse.length < 3 || !isValidDate(parse[2])) {
				return null;
			}

			String numeroSerie = parse[0];
			String secret = parse[1];
			String dateString = parse[2];

			if (!validarKeyBase(secret)) {
				return null;
			}

			LocalDateTime tokenTime = LocalDateTime.parse(dateString, dtfPadrao);
			if (isTokenExpired(tokenTime, TIME)) {
				throw new IOException("Token inválido");
			}

			return new AuthDeviceRecords(numeroSerie, secret, dateString);
		} catch (IOException | IllegalArgumentException e) {
			return null;
		}
	}

	public static AuthDeviceRecords validarTokenBaseString(String token) throws IOException {
		String decodedToken = Utils.decode(token);
		System.out.println("Decoded Token: " + decodedToken);
		String[] parse = splitSenha(decodedToken, "_");
		if (parse.length < 3 || !isValidDate(parse[2])) {
			throw new IOException("Token inválido");
		}

		String numeroSerie = parse[0];
		String secret = parse[1];
		String dateString = parse[2];

		if (!validarKeyBase(secret)) {
			throw new IOException("Token inválido");
		}

		LocalDateTime tokenTime = LocalDateTime.parse(dateString, dtfPadrao);
		if (isTokenExpired(tokenTime, TIME)) {
			throw new IOException("Token inválido");
		}

		return new AuthDeviceRecords(numeroSerie, secret, dateString);
	}

	private static boolean isTokenExpired(LocalDateTime tokenTime, long expirationTimeInMinutes) {
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		long minutesElapsed = Duration.between(tokenTime, now).toMinutes();
		return minutesElapsed > expirationTimeInMinutes;
	}

	public static String addHoursToDateString(String dateString, long minutesToAdd) {
		LocalDateTime dateTime = LocalDateTime.parse(dateString, dtfPadrao);
		LocalDateTime newDateTime = dateTime.plusMinutes(minutesToAdd);
		return dtfPadrao.format(newDateTime);
	}

//	public ResponseDeviceDTO gerarTokenDevice(AuthDeviceModel auth) throws EntityNotFoundException, IOException {
//		Objects.requireNonNull(auth.getNse(), "Login está nulo.");
//		Objects.requireNonNull(auth.getSenha(), "Senha está nula.");
//
//		var modulo = sireneModuloService.findBySmonseEntity(auth.getNse());
//		if (modulo == null) {
//			throw new IOException("Não foi encontrado nenhum módulo compatível com esse número de série do Login: " + auth.getNse());
//		}
//
//		String[] parse = splitSenha(auth.getSenha(), "@");
//		if (parse == null || parse.length < 2 || !isValidDate(parse[1])) {
//			throw new IOException("Senha não é compatível com o modelo esperado.");
//		}
//
//		var nseSenha = sireneModuloService.findBySmonseEntity(parse[0]);
//		if (nseSenha == null) {
//			throw new IOException("Não foi encontrado nenhum módulo compatível com esse número de série na senha: " + auth.getSenha());
//		}
//
//		String dataToken = parse[1];
//		String newDataToken = addHoursToDateString(dataToken, TIME);
//
//		var token = gerarTokenBase(auth.getSenha());
//		if (token == null) {
//			throw new IOException("Erro ao gerar token. Senha não é compatível com o modelo esperado.");
//		}
//		return new ResponseDeviceDTO(token, dataToken, newDataToken);
//	}

	public TokenValidationDeviceDTO validarTokenDevice(String token) throws IOException {
		var isValid = validarTokenBaseString(token);
		if (isValid == null)
			throw new IOException("Token Inválido");

		String newDataToken = addHoursToDateString(isValid.date(), TIME);

		return new TokenValidationDeviceDTO(validarTokenBase(token), isValid.date(), newDataToken);
	}
}
