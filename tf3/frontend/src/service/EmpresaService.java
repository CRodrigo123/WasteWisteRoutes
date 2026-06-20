package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.Empresa;
import com.naturenext.wastewiseroutes.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> listar() {
        return empresaRepository.findAll();
    }

    public Optional<Empresa> obtenerPorId(Long id) {
        return empresaRepository.findById(id);
    }

    public Empresa guardar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public Optional<Empresa> actualizar(Long id, Empresa datos) {
        return empresaRepository.findById(id).map(e -> {
            e.setNombre(datos.getNombre());
            e.setRuc(datos.getRuc());
            e.setTelefono(datos.getTelefono());
            e.setDireccion(datos.getDireccion());
            return empresaRepository.save(e);
        });
    }

    public boolean eliminar(Long id) {
        if (empresaRepository.existsById(id)) {
            empresaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
