package com.naturenext.wastewiseroutes.repository;

import com.naturenext.wastewiseroutes.model.ReporteEstadistico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteEstadisticoRepository extends JpaRepository<ReporteEstadistico, Long> {
}
