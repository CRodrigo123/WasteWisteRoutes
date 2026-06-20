package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.Reporte;
import com.naturenext.wastewiseroutes.security.SecurityUtils;
import com.naturenext.wastewiseroutes.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Reporte")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<Reporte> listar() {
        return reporteService.listar();
    }

    @GetMapping("/mios")
    @PreAuthorize("isAuthenticated()")
    public List<Reporte> listarMios(Authentication authentication) {
        return reporteService.listarMios(SecurityUtils.usuarioActual(authentication));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Reporte> obtenerPorId(@PathVariable Long id) {
        return reporteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mios/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Reporte> obtenerMioPorId(@PathVariable Long id, Authentication authentication) {
        return reporteService.obtenerMioPorId(id, SecurityUtils.usuarioActual(authentication))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Reporte> crear(@Valid @RequestBody Reporte reporte) {
        return ResponseEntity.ok(reporteService.guardar(reporte));
    }

    @PostMapping("/mios")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Reporte> crearMio(@Valid @RequestBody Reporte reporte, Authentication authentication) {
        return ResponseEntity.ok(reporteService.guardarMio(reporte, SecurityUtils.usuarioActual(authentication)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Reporte> actualizar(@PathVariable Long id, @Valid @RequestBody Reporte reporte) {
        return reporteService.actualizar(id, reporte)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/mios/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Reporte> actualizarMio(@PathVariable Long id, @Valid @RequestBody Reporte reporte, Authentication authentication) {
        return reporteService.actualizarMio(id, reporte, SecurityUtils.usuarioActual(authentication))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (reporteService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/mios/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminarMio(@PathVariable Long id, Authentication authentication) {
        if (reporteService.eliminarMio(id, SecurityUtils.usuarioActual(authentication))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
