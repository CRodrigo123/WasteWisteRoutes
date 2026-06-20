package com.naturenext.wastewiseroutes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String DISTANCE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";

    public Map geocodificarDireccion(String direccion) {
        String url = UriComponentsBuilder.fromHttpUrl(GEOCODE_URL)
                .queryParam("address", direccion)
                .queryParam("key", apiKey)
                .toUriString();
        return restTemplate.getForObject(url, Map.class);
    }

    public Map geocodificarInverso(Double latitud, Double longitud) {
        String latlng = latitud + "," + longitud;
        String url = UriComponentsBuilder.fromHttpUrl(GEOCODE_URL)
                .queryParam("latlng", latlng)
                .queryParam("key", apiKey)
                .toUriString();
        return restTemplate.getForObject(url, Map.class);
    }

    public Map calcularDistancia(String origen, String destino) {
        String url = UriComponentsBuilder.fromHttpUrl(DISTANCE_URL)
                .queryParam("origins", origen)
                .queryParam("destinations", destino)
                .queryParam("mode", "driving")
                .queryParam("language", "es")
                .queryParam("key", apiKey)
                .toUriString();
        return restTemplate.getForObject(url, Map.class);
    }
}
