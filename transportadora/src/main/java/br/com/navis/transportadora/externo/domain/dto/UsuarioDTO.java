package br.com.navis.transportadora.externo.domain.dto;

import br.com.navis.motorista.domain.models.Usuario;
import lombok.Builder;

@Builder
public record UsuarioDTO(
        String id,
        String login,
        String role
) {

    public static UsuarioDTO of(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .login(usuario.getLogin())
                .role(usuario.getRole().toString())
                .build();
    }

}
