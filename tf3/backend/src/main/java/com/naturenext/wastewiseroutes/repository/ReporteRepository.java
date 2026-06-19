package com.naturenext.wastewiseroutes.repository;

import com.naturenext.wastewiseroutes.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByUsuarioId(Long usuarioId);
    Optional<Reporte> findByIdAndUsuarioId(Long id, Long usuarioId);
}
