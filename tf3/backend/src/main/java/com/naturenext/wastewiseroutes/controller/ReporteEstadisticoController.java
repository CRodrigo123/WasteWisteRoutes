package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.ReporteEstadistico;
import com.naturenext.wastewiseroutes.service.ReporteEstadisticoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ReporteEstadistico")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ReporteEstadisticoController {

    @Autowired
    private ReporteEstadisticoService reporteEstadisticoService;

    @GetMapping
    public List<ReporteEstadistico> listar() {
        return reporteEstadisticoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteEstadistico> obtenerPorId(@PathVariable Long id) {
        return reporteEstadisticoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReporteEstadistico> crear(@Valid @RequestBody ReporteEstadistico reporteEstadistico) {
        return ResponseEntity.ok(reporteEstadisticoService.guardar(reporteEstadistico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteEstadistico> actualizar(@PathVariable Long id, @Valid @RequestBody ReporteEstadistico reporteEstadistico) {
        return reporteEstadisticoService.actualizar(id, reporteEstadistico)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (reporteEstadisticoService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
