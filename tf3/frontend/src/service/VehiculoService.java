package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.Vehiculo;
import com.naturenext.wastewiseroutes.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public List<Vehiculo> listar() {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    public Vehiculo guardar(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    public Optional<Vehiculo> actualizar(Long id, Vehiculo datos) {
        return vehiculoRepository.findById(id).map(v -> {
            v.setPlaca(datos.getPlaca());
            v.setCapacidad(datos.getCapacidad());
            v.setEstado(datos.getEstado());
            v.setEmpresa(datos.getEmpresa());
            return vehiculoRepository.save(v);
        });
    }

    public boolean eliminar(Long id) {
        if (vehiculoRepository.existsById(id)) {
            vehiculoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
