package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.Evidencia;
import com.naturenext.wastewiseroutes.security.SecurityUtils;
import com.naturenext.wastewiseroutes.service.EvidenciaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Evidencia")
public class EvidenciaController {

    private final EvidenciaService evidenciaService;

    public EvidenciaController(EvidenciaService evidenciaService) {
        this.evidenciaService = evidenciaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<Evidencia> listar() {
        return evidenciaService.listar();
    }

    @GetMapping("/mias")
    @PreAuthorize("isAuthenticated()")
    public List<Evidencia> listarMias(Authentication authentication) {
        return evidenciaService.listarMias(SecurityUtils.usuarioActual(authentication));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Evidencia> obtenerPorId(@PathVariable Long id) {
        return evidenciaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Evidencia> crear(@Valid @RequestBody Evidencia evidencia) {
        return ResponseEntity.ok(evidenciaService.guardar(evidencia));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Evidencia> actualizar(@PathVariable Long id, @Valid @RequestBody Evidencia evidencia) {
        return evidenciaService.actualizar(id, evidencia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (evidenciaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
