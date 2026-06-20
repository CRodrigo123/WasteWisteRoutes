package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.Ubicacion;
import com.naturenext.wastewiseroutes.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    public List<Ubicacion> listar() {
        return ubicacionRepository.findAll();
    }

    public Optional<Ubicacion> obtenerPorId(Long id) {
        return ubicacionRepository.findById(id);
    }

    public Ubicacion guardar(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    public Optional<Ubicacion> actualizar(Long id, Ubicacion datos) {
        return ubicacionRepository.findById(id).map(u -> {
            u.setLatitud(datos.getLatitud());
            u.setLongitud(datos.getLongitud());
            u.setDireccion(datos.getDireccion());
            return ubicacionRepository.save(u);
        });
    }

    public boolean eliminar(Long id) {
        if (ubicacionRepository.existsById(id)) {
            ubicacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
