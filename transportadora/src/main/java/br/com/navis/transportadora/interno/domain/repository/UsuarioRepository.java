package br.com.navis.transportadora.interno.domain.repository;

import br.com.navis.transportadora.interno.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    UserDetails findByLogin(String login);

}
