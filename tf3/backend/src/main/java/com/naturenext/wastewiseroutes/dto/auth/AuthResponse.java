package com.naturenext.wastewiseroutes.dto.auth;

import com.naturenext.wastewiseroutes.dto.usuario.UsuarioResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UsuarioResponse usuario;
}
