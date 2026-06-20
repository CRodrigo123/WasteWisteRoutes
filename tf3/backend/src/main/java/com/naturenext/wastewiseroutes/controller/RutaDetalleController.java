package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.RutaDetalle;
import com.naturenext.wastewiseroutes.security.SecurityUtils;
import com.naturenext.wastewiseroutes.service.RutaDetalleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/RutaDetalle")
public class RutaDetalleController {

    private final RutaDetalleService rutaDetalleService;

    public RutaDetalleController(RutaDetalleService rutaDetalleService) {
        this.rutaDetalleService = rutaDetalleService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<RutaDetalle> listar() {
        return rutaDetalleService.listar();
    }

    @GetMapping("/mias")
    @PreAuthorize("isAuthenticated()")
    public List<RutaDetalle> listarMias(Authentication authentication) {
        return rutaDetalleService.listarMios(SecurityUtils.usuarioActual(authentication));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RutaDetalle> obtenerPorId(@PathVariable Long id) {
        return rutaDetalleService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RutaDetalle> crear(@Valid @RequestBody RutaDetalle rutaDetalle) {
        return ResponseEntity.ok(rutaDetalleService.guardar(rutaDetalle));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RutaDetalle> actualizar(@PathVariable Long id, @Valid @RequestBody RutaDetalle rutaDetalle) {
        return rutaDetalleService.actualizar(id, rutaDetalle)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (rutaDetalleService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
