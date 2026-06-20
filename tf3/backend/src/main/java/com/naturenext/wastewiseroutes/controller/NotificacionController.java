package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.Notificacion;
import com.naturenext.wastewiseroutes.security.SecurityUtils;
import com.naturenext.wastewiseroutes.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Notificacion")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<Notificacion> listar() {
        return notificacionService.listar();
    }

    @GetMapping("/mias")
    @PreAuthorize("isAuthenticated()")
    public List<Notificacion> listarMias(Authentication authentication) {
        return notificacionService.listarMias(SecurityUtils.usuarioActual(authentication));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Notificacion> obtenerPorId(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mias/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Notificacion> obtenerMiaPorId(@PathVariable Long id, Authentication authentication) {
        return notificacionService.obtenerMiaPorId(id, SecurityUtils.usuarioActual(authentication))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/mias/{id}/leer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Notificacion> marcarMiaComoLeida(@PathVariable Long id, Authentication authentication) {
        return notificacionService.marcarMiaComoLeida(id, SecurityUtils.usuarioActual(authentication))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Notificacion> crear(@Valid @RequestBody Notificacion notificacion) {
        return ResponseEntity.ok(notificacionService.guardar(notificacion));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Notificacion> actualizar(@PathVariable Long id, @Valid @RequestBody Notificacion notificacion) {
        return notificacionService.actualizar(id, notificacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (notificacionService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
