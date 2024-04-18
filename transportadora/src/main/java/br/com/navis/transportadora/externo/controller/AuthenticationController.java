package br.com.navis.transportadora.externo.controller;

import br.com.navis.transportadora.externo.domain.dto.AuthenticationDTO;
import br.com.navis.transportadora.externo.domain.dto.LoginResponseDTO;
import br.com.navis.transportadora.externo.domain.dto.RegisterDTO;
import br.com.navis.transportadora.externo.domain.dto.UsuarioDTO;
import br.com.navis.transportadora.interno.domain.model.Usuario;
import br.com.navis.transportadora.interno.domain.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.navis.transportadora.interno.infra.TokenService;

@RestController
@RequestMapping("/transportadora/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UsuarioRepository repository;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = tokenService.generateToken(usuario);

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getId(), usuario.getLogin(), usuario.getRole().toString());
        LoginResponseDTO responseDTO = new LoginResponseDTO(usuarioDTO, token);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Usuario newUser = new Usuario(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

}
