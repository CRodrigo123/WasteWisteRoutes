package com.naturenext.wastewiseroutes.repository;

import com.naturenext.wastewiseroutes.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    @Query("select distinct r.ruta from Reporte r where r.usuario.id = :usuarioId and r.ruta is not null")
    List<Ruta> findRutasByUsuarioId(@Param("usuarioId") Long usuarioId);
}
