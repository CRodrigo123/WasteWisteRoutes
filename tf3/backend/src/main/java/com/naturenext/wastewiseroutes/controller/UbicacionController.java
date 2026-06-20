package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.Ubicacion;
import com.naturenext.wastewiseroutes.service.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Ubicacion")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping
    public List<Ubicacion> listar() {
        return ubicacionService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> obtenerPorId(@PathVariable Long id) {
        return ubicacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ubicacion> crear(@Valid @RequestBody Ubicacion ubicacion) {
        return ResponseEntity.ok(ubicacionService.guardar(ubicacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ubicacion> actualizar(@PathVariable Long id, @Valid @RequestBody Ubicacion ubicacion) {
        return ubicacionService.actualizar(id, ubicacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (ubicacionService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
