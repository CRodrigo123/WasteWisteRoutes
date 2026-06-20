package com.naturenext.wastewiseroutes.dto.usuario;

import com.naturenext.wastewiseroutes.model.Usuario;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String correo;
    private String tipo;
    private LocalDateTime fechaRegistro;

    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getTipo(),
                usuario.getFechaRegistro()
        );
    }
}
