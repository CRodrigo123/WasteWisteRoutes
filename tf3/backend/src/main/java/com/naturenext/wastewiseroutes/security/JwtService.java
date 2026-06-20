package com.naturenext.wastewiseroutes.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturenext.wastewiseroutes.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtService {

    private final String secret;
    private final long expirationMs;
    private final ObjectMapper objectMapper;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs,
            ObjectMapper objectMapper
    ) {
        this.secret = secret;
        this.expirationMs = expirationMs;
        this.objectMapper = objectMapper;
    }

    public String generarToken(Usuario usuario) {
        try {
            long ahora = Instant.now().toEpochMilli();
            long expira = ahora + expirationMs;

            Map<String, Object> header = new LinkedHashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("sub", usuario.getCorreo());
            payload.put("id", usuario.getId());
            payload.put("nombre", usuario.getNombre());
            payload.put("tipo", usuario.getTipo() == null || usuario.getTipo().isBlank() ? "CLIENTE" : usuario.getTipo());
            payload.put("iat", ahora);
            payload.put("exp", expira);

            String headerBase64 = encodeJson(header);
            String payloadBase64 = encodeJson(payload);
            String firma = firmar(headerBase64 + "." + payloadBase64);

            return headerBase64 + "." + payloadBase64 + "." + firma;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el token JWT", e);
        }
    }

    public Optional<AuthenticatedUser> validarToken(String token) {
        try {
            String[] partes = token.split("\\.");
            if (partes.length != 3) return Optional.empty();

            String contenido = partes[0] + "." + partes[1];
            String firmaEsperada = firmar(contenido);
            if (!constantTimeEquals(firmaEsperada, partes[2])) return Optional.empty();

            Map<String, Object> payload = objectMapper.readValue(
                    Base64.getUrlDecoder().decode(partes[1]),
                    new TypeReference<Map<String, Object>>() {}
            );

            long exp = ((Number) payload.get("exp")).longValue();
            if (Instant.now().toEpochMilli() > exp) return Optional.empty();

            Long id = ((Number) payload.get("id")).longValue();
            String correo = String.valueOf(payload.get("sub"));
            String nombre = String.valueOf(payload.get("nombre"));
            String tipo = payload.get("tipo") == null ? "CLIENTE" : String.valueOf(payload.get("tipo"));

            return Optional.of(new AuthenticatedUser(id, correo, nombre, tipo));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String encodeJson(Map<String, Object> data) throws Exception {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(objectMapper.writeValueAsBytes(data));
    }

    private String firmar(String contenido) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(key);
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(mac.doFinal(contenido.getBytes(StandardCharsets.UTF_8)));
    }

    private boolean constantTimeEquals(String a, String b) {
        byte[] bytesA = a.getBytes(StandardCharsets.UTF_8);
        byte[] bytesB = b.getBytes(StandardCharsets.UTF_8);
        if (bytesA.length != bytesB.length) return false;
        int result = 0;
        for (int i = 0; i < bytesA.length; i++) {
            result |= bytesA[i] ^ bytesB[i];
        }
        return result == 0;
    }
}
