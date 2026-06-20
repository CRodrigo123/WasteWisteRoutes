package com.naturenext.wastewiseroutes.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedUser {
    private Long id;
    private String correo;
    private String nombre;
    private String tipo;
}
