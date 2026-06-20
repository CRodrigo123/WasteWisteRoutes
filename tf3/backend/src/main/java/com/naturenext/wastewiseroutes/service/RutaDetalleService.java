package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.RutaDetalle;
import com.naturenext.wastewiseroutes.repository.RutaDetalleRepository;
import com.naturenext.wastewiseroutes.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RutaDetalleService {

    private final RutaDetalleRepository rutaDetalleRepository;

    public RutaDetalleService(RutaDetalleRepository rutaDetalleRepository) {
        this.rutaDetalleRepository = rutaDetalleRepository;
    }

    public List<RutaDetalle> listar() {
        return rutaDetalleRepository.findAll();
    }

    public List<RutaDetalle> listarMios(AuthenticatedUser usuario) {
        return rutaDetalleRepository.findDetallesByUsuarioId(usuario.getId());
    }

    public Optional<RutaDetalle> obtenerPorId(Long id) {
        return rutaDetalleRepository.findById(id);
    }

    public RutaDetalle guardar(RutaDetalle rutaDetalle) {
        return rutaDetalleRepository.save(rutaDetalle);
    }

    public Optional<RutaDetalle> actualizar(Long id, RutaDetalle datos) {
        return rutaDetalleRepository.findById(id).map(rd -> {
            rd.setOrden(datos.getOrden());
            rd.setRuta(datos.getRuta());
            rd.setUbicacion(datos.getUbicacion());
            return rutaDetalleRepository.save(rd);
        });
    }

    public boolean eliminar(Long id) {
        if (rutaDetalleRepository.existsById(id)) {
            rutaDetalleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
