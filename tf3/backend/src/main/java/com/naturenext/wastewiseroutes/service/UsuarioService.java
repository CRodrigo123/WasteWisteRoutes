package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.dto.usuario.UsuarioRequest;
import com.naturenext.wastewiseroutes.dto.usuario.UsuarioResponse;
import com.naturenext.wastewiseroutes.model.Usuario;
import com.naturenext.wastewiseroutes.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponse::fromEntity)
                .toList();
    }

    public Optional<UsuarioResponse> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(UsuarioResponse::fromEntity);
    }

    public UsuarioResponse guardar(UsuarioRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTipo(normalizarTipo(request.getTipo()));
        return UsuarioResponse.fromEntity(usuarioRepository.save(usuario));
    }

    public Optional<UsuarioResponse> actualizar(Long id, UsuarioRequest datos) {
        return usuarioRepository.findById(id).map(u -> {
            usuarioRepository.findByCorreo(datos.getCorreo()).ifPresent(usuarioExistente -> {
                if (!usuarioExistente.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
                }
            });
            u.setNombre(datos.getNombre());
            u.setCorreo(datos.getCorreo());
            if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                u.setPassword(passwordEncoder.encode(datos.getPassword()));
            }
            u.setTipo(normalizarTipo(datos.getTipo()));
            return UsuarioResponse.fromEntity(usuarioRepository.save(u));
        });
    }

    public boolean eliminar(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) return "CLIENTE";
        String normalizado = tipo.trim().toUpperCase();
        if ("CIUDADANO".equals(normalizado)) return "CLIENTE";
        return normalizado;
    }
}
