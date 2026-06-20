package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.Ruta;
import com.naturenext.wastewiseroutes.repository.RutaRepository;
import com.naturenext.wastewiseroutes.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    public List<Ruta> listar() {
        return rutaRepository.findAll();
    }

    public List<Ruta> listarMias(AuthenticatedUser usuario) {
        return rutaRepository.findRutasByUsuarioId(usuario.getId());
    }

    public Optional<Ruta> obtenerPorId(Long id) {
        return rutaRepository.findById(id);
    }

    public Ruta guardar(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public Optional<Ruta> actualizar(Long id, Ruta datos) {
        return rutaRepository.findById(id).map(r -> {
            r.setFecha(datos.getFecha());
            r.setEstado(datos.getEstado());
            r.setEmpresa(datos.getEmpresa());
            r.setVehiculo(datos.getVehiculo());
            return rutaRepository.save(r);
        });
    }

    public boolean eliminar(Long id) {
        if (rutaRepository.existsById(id)) {
            rutaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
