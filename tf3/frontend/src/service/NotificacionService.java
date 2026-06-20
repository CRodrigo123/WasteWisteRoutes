package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.Notificacion;
import com.naturenext.wastewiseroutes.repository.NotificacionRepository;
import com.naturenext.wastewiseroutes.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> listar() {
        return notificacionRepository.findAll();
    }

    public List<Notificacion> listarMias(AuthenticatedUser usuario) {
        return notificacionRepository.findByUsuarioId(usuario.getId());
    }

    public Optional<Notificacion> obtenerPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    public Optional<Notificacion> obtenerMiaPorId(Long id, AuthenticatedUser usuario) {
        return notificacionRepository.findByIdAndUsuarioId(id, usuario.getId());
    }

    public Notificacion guardar(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public Optional<Notificacion> actualizar(Long id, Notificacion datos) {
        return notificacionRepository.findById(id).map(n -> {
            n.setMensaje(datos.getMensaje());
            n.setLeido(datos.getLeido());
            n.setUsuario(datos.getUsuario());
            return notificacionRepository.save(n);
        });
    }

    public Optional<Notificacion> marcarMiaComoLeida(Long id, AuthenticatedUser usuario) {
        return notificacionRepository.findByIdAndUsuarioId(id, usuario.getId()).map(n -> {
            n.setLeido(true);
            return notificacionRepository.save(n);
        });
    }

    public boolean eliminar(Long id) {
        if (notificacionRepository.existsById(id)) {
            notificacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
