package com.naturenext.wastewiseroutes.controller;

import com.naturenext.wastewiseroutes.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/maps")
public class GoogleMapsController {

    @Autowired
    private GoogleMapsService googleMapsService;

    @GetMapping("/geocodificar")
    public Map geocodificar(@RequestParam String direccion) {
        return googleMapsService.geocodificarDireccion(direccion);
    }

    @GetMapping("/geocodificar-inverso")
    public Map geocodificarInverso(
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        return googleMapsService.geocodificarInverso(latitud, longitud);
    }

    @GetMapping("/distancia")
    public Map calcularDistancia(
            @RequestParam String origen,
            @RequestParam String destino) {
        return googleMapsService.calcularDistancia(origen, destino);
    }
}
