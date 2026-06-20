package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.dto.auth.AuthResponse;
import com.naturenext.wastewiseroutes.dto.auth.LoginRequest;
import com.naturenext.wastewiseroutes.dto.auth.RegisterRequest;
import com.naturenext.wastewiseroutes.dto.usuario.UsuarioResponse;
import com.naturenext.wastewiseroutes.model.Usuario;
import com.naturenext.wastewiseroutes.repository.UsuarioRepository;
import com.naturenext.wastewiseroutes.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        String tipoSolicitado = normalizarTipo(request.getTipo());
        String tipoFinal = tipoParaRegistroPublico(tipoSolicitado);

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTipo(tipoFinal);

        Usuario guardado = usuarioRepository.save(usuario);
        String token = jwtService.generarToken(guardado);
        return new AuthResponse(token, UsuarioResponse.fromEntity(guardado));
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        if (!passwordValida(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        // Migración suave: si el usuario existía con contraseña en texto plano, se regraba en BCrypt.
        if (!esBCrypt(usuario.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuarioRepository.save(usuario);
        }

        String token = jwtService.generarToken(usuario);
        return new AuthResponse(token, UsuarioResponse.fromEntity(usuario));
    }

    private boolean passwordValida(String rawPassword, String storedPassword) {
        if (storedPassword == null) return false;
        if (esBCrypt(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }
        return rawPassword.equals(storedPassword);
    }

    private boolean esBCrypt(String password) {
        return password != null && (
                password.startsWith("$2a$") ||
                password.startsWith("$2b$") ||
                password.startsWith("$2y$")
        );
    }

    private String tipoParaRegistroPublico(String tipoSolicitado) {
        boolean existeAdmin = usuarioRepository.existsByTipoIgnoreCase("ADMINISTRADOR");

        // Permite crear el primer administrador desde /auth/register.
        // Después de eso, el registro público crea clientes normales.
        if (!existeAdmin && "ADMINISTRADOR".equals(tipoSolicitado)) {
            return "ADMINISTRADOR";
        }

        if ("ADMINISTRADOR".equals(tipoSolicitado)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo un administrador puede crear otros administradores");
        }

        return "CLIENTE";
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) return "CLIENTE";
        String normalizado = tipo.trim().toUpperCase();
        if ("CIUDADANO".equals(normalizado)) return "CLIENTE";
        return normalizado;
    }
}
