package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.Reporte;
import com.naturenext.wastewiseroutes.model.Usuario;
import com.naturenext.wastewiseroutes.repository.ReporteRepository;
import com.naturenext.wastewiseroutes.repository.UsuarioRepository;
import com.naturenext.wastewiseroutes.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteService(ReporteRepository reporteRepository, UsuarioRepository usuarioRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Reporte> listar() {
        return reporteRepository.findAll();
    }

    public List<Reporte> listarMios(AuthenticatedUser usuario) {
        return reporteRepository.findByUsuarioId(usuario.getId());
    }

    public Optional<Reporte> obtenerPorId(Long id) {
        return reporteRepository.findById(id);
    }

    public Optional<Reporte> obtenerMioPorId(Long id, AuthenticatedUser usuario) {
        return reporteRepository.findByIdAndUsuarioId(id, usuario.getId());
    }

    public Reporte guardar(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public Reporte guardarMio(Reporte reporte, AuthenticatedUser usuarioAutenticado) {
        Usuario usuario = usuarioRepository.findById(usuarioAutenticado.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
        reporte.setUsuario(usuario);
        reporte.setEstado("PENDIENTE");
        reporte.setEmpresa(null);
        reporte.setRuta(null);
        return reporteRepository.save(reporte);
    }

    public Optional<Reporte> actualizar(Long id, Reporte datos) {
        return reporteRepository.findById(id).map(r -> {
            r.setDescripcion(datos.getDescripcion());
            r.setEstado(datos.getEstado());
            r.setUsuario(datos.getUsuario());
            r.setUbicacion(datos.getUbicacion());
            r.setEmpresa(datos.getEmpresa());
            r.setRuta(datos.getRuta());
            return reporteRepository.save(r);
        });
    }

    public Optional<Reporte> actualizarMio(Long id, Reporte datos, AuthenticatedUser usuarioAutenticado) {
        return reporteRepository.findByIdAndUsuarioId(id, usuarioAutenticado.getId()).map(r -> {
            // El cliente solo puede modificar datos propios del reporte.
            // No puede reasignar usuario, empresa, ruta ni estado desde este endpoint.
            r.setDescripcion(datos.getDescripcion());
            r.setUbicacion(datos.getUbicacion());
            return reporteRepository.save(r);
        });
    }

    public boolean eliminar(Long id) {
        if (reporteRepository.existsById(id)) {
            reporteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean eliminarMio(Long id, AuthenticatedUser usuarioAutenticado) {
        return reporteRepository.findByIdAndUsuarioId(id, usuarioAutenticado.getId()).map(reporte -> {
            reporteRepository.delete(reporte);
            return true;
        }).orElse(false);
    }
}
