package com.naturenext.wastewiseroutes.repository;

import com.naturenext.wastewiseroutes.model.RutaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaDetalleRepository extends JpaRepository<RutaDetalle, Long> {
    @Query("select rd from RutaDetalle rd where rd.ruta.id in " +
           "(select distinct r.ruta.id from Reporte r where r.usuario.id = :usuarioId and r.ruta is not null) " +
           "order by rd.ruta.id, rd.orden")
    List<RutaDetalle> findDetallesByUsuarioId(@Param("usuarioId") Long usuarioId);
}
