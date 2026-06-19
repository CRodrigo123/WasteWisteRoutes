package com.naturenext.wastewiseroutes.repository;

import com.naturenext.wastewiseroutes.model.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenciaRepository extends JpaRepository<Evidencia, Long> {
    List<Evidencia> findByReporteUsuarioId(Long usuarioId);
}
