package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.Ruta;
import com.naturenext.wastewiseroutes.security.SecurityUtils;
import com.naturenext.wastewiseroutes.service.RutaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Ruta")
public class RutaController {

    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<Ruta> listar() {
        return rutaService.listar();
    }

    @GetMapping("/mias")
    @PreAuthorize("isAuthenticated()")
    public List<Ruta> listarMias(Authentication authentication) {
        return rutaService.listarMias(SecurityUtils.usuarioActual(authentication));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Ruta> obtenerPorId(@PathVariable Long id) {
        return rutaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Ruta> crear(@Valid @RequestBody Ruta ruta) {
        return ResponseEntity.ok(rutaService.guardar(ruta));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Ruta> actualizar(@PathVariable Long id, @Valid @RequestBody Ruta ruta) {
        return rutaService.actualizar(id, ruta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (rutaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
