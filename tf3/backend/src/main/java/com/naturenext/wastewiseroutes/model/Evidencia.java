package com.naturenext.wastewiseroutes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "evidencia")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_imagen", columnDefinition = "TEXT")
    private String urlImagen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reporte_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Reporte reporte;
}
