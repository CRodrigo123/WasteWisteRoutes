package com.naturenext.wastewiseroutes.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static AuthenticatedUser usuarioActual(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser usuario)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return usuario;
    }

    public static boolean esAdmin(Authentication authentication) {
        AuthenticatedUser usuario = usuarioActual(authentication);
        return tieneRol(usuario, "ADMINISTRADOR");
    }

    public static boolean tieneRol(AuthenticatedUser usuario, String rol) {
        return usuario != null
                && usuario.getTipo() != null
                && usuario.getTipo().trim().equalsIgnoreCase(rol);
    }

    public static boolean esCliente(AuthenticatedUser usuario) {
        return tieneRol(usuario, "CLIENTE") || tieneRol(usuario, "CIUDADANO");
    }
}
