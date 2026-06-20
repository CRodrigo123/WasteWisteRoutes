package com.naturenext.wastewiseroutes.service;

import com.naturenext.wastewiseroutes.model.ReporteEstadistico;
import com.naturenext.wastewiseroutes.repository.ReporteEstadisticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteEstadisticoService {

    @Autowired
    private ReporteEstadisticoRepository reporteEstadisticoRepository;

    public List<ReporteEstadistico> listar() {
        return reporteEstadisticoRepository.findAll();
    }

    public Optional<ReporteEstadistico> obtenerPorId(Long id) {
        return reporteEstadisticoRepository.findById(id);
    }

    public ReporteEstadistico guardar(ReporteEstadistico reporteEstadistico) {
        return reporteEstadisticoRepository.save(reporteEstadistico);
    }

    public Optional<ReporteEstadistico> actualizar(Long id, ReporteEstadistico datos) {
        return reporteEstadisticoRepository.findById(id).map(re -> {
            re.setFechaInicio(datos.getFechaInicio());
            re.setFechaFin(datos.getFechaFin());
            re.setTotalReportes(datos.getTotalReportes());
            re.setAhorroCombustible(datos.getAhorroCombustible());
            re.setEmpresa(datos.getEmpresa());
            return reporteEstadisticoRepository.save(re);
        });
    }

    public boolean eliminar(Long id) {
        if (reporteEstadisticoRepository.existsById(id)) {
            reporteEstadisticoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
