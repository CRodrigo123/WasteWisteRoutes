package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.Evidencia;
import com.naturenext.wastewiseroutes.repository.EvidenciaRepository;
import com.naturenext.wastewiseroutes.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;

    public EvidenciaService(EvidenciaRepository evidenciaRepository) {
        this.evidenciaRepository = evidenciaRepository;
    }

    public List<Evidencia> listar() {
        return evidenciaRepository.findAll();
    }

    public List<Evidencia> listarMias(AuthenticatedUser usuario) {
        return evidenciaRepository.findByReporteUsuarioId(usuario.getId());
    }

    public Optional<Evidencia> obtenerPorId(Long id) {
        return evidenciaRepository.findById(id);
    }

    public Evidencia guardar(Evidencia evidencia) {
        return evidenciaRepository.save(evidencia);
    }

    public Optional<Evidencia> actualizar(Long id, Evidencia datos) {
        return evidenciaRepository.findById(id).map(e -> {
            e.setUrlImagen(datos.getUrlImagen());
            e.setReporte(datos.getReporte());
            return evidenciaRepository.save(e);
        });
    }

    public boolean eliminar(Long id) {
        if (evidenciaRepository.existsById(id)) {
            evidenciaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
