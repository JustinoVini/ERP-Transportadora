package br.com.navis.transportadora.externo.domain.dto;

import br.com.navis.transportadora.interno.domain.model.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
