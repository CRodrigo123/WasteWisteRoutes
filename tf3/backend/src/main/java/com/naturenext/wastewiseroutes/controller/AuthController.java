package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.dto.auth.AuthResponse;
import com.naturenext.wastewiseroutes.dto.auth.LoginRequest;
import com.naturenext.wastewiseroutes.dto.auth.RegisterRequest;
import com.naturenext.wastewiseroutes.dto.usuario.UsuarioResponse;
import com.naturenext.wastewiseroutes.model.Usuario;
import com.naturenext.wastewiseroutes.repository.UsuarioRepository;
import com.naturenext.wastewiseroutes.security.AuthenticatedUser;
import com.naturenext.wastewiseroutes.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthService authService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registrar(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> perfil(Authentication authentication) {
        AuthenticatedUser autenticado = (AuthenticatedUser) authentication.getPrincipal();
        return usuarioRepository.findById(autenticado.getId())
                .map(UsuarioResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
