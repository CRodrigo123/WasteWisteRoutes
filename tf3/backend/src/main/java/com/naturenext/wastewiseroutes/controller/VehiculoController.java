package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.Vehiculo;
import com.naturenext.wastewiseroutes.service.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Vehiculo")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public List<Vehiculo> listar() {
        return vehiculoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        return vehiculoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.guardar(vehiculo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id, @Valid @RequestBody Vehiculo vehiculo) {
        return vehiculoService.actualizar(id, vehiculo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (vehiculoService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
