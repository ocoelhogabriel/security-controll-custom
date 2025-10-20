package com.ocoelhogabriel.security_control_custom.application.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UrlPermissoesDTO {
	List<String> buscar = new ArrayList<>();
	List<String> deletar = new ArrayList<>();
	List<String> criar = new ArrayList<>();
	List<String> editar = new ArrayList<>();
	List<String> perfil = new ArrayList<>();

	public void limparPermissao() {
		buscar.clear();
		deletar.clear();
		criar.clear();
		editar.clear();
		perfil.clear();
	}

	public void adicionarPermissao(String metodo, String url) {
		switch (metodo) {
		case "GET":
			buscar.add(url);
			break;
		case "DELETE":
			deletar.add(url);
			break;
		case "POST":
			criar.add(url);
			break;
		case "PUT":
			editar.add(url);
			break;
		}
	}

	public String[] getBuscar() {
		return buscar.toArray(String[]::new);
	}

	public String[] getDeletar() {
		return deletar.toArray(String[]::new);
	}

	public String[] getCriar() {
		return criar.toArray(String[]::new);
	}

	public String[] getEditar() {
		return editar.toArray(String[]::new);
	}

	public String[] getPerfil() {
		return perfil.toArray(String[]::new);
	}

	public void setPerfil(PerfilDTO listaPerfil) {
		perfil.add(listaPerfil.getDescricao().toUpperCase());
		// System.out.println(perfil.toString());
	}

	public void setPerfil(List<PerfilDTO> listaPerfil) {
		for (PerfilDTO per : listaPerfil) {
			perfil.add(per.getDescricao().toUpperCase());
		}
		// System.out.println(perfil.toString());
	}

	public void setPerfil(String str) {
		perfil.add(str);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UrlPermissoesDTO [");
		if (buscar != null) {
			builder.append("buscar=").append(buscar).append(", ");
		}
		if (deletar != null) {
			builder.append("deletar=").append(deletar).append(", ");
		}
		if (criar != null) {
			builder.append("criar=").append(criar).append(", ");
		}
		if (editar != null) {
			builder.append("editar=").append(editar).append(", ");
		}
		if (perfil != null) {
			builder.append("perfil=").append(perfil);
		}
		builder.append("]");
		return builder.toString();
	}

}
