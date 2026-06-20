package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.model.Empresa;
import com.naturenext.wastewiseroutes.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Empresa")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public List<Empresa> listar() {
        return empresaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obtenerPorId(@PathVariable Long id) {
        return empresaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empresa> crear(@Valid @RequestBody Empresa empresa) {
        return ResponseEntity.ok(empresaService.guardar(empresa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> actualizar(@PathVariable Long id, @Valid @RequestBody Empresa empresa) {
        return empresaService.actualizar(id, empresa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (empresaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
