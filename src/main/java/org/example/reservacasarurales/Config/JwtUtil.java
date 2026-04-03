package org.example.reservacasarurales.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;



@Component
public class JwtUtil {
    private final String SECRET_KEY = "EstaEsUnaClaveSuperSecretaDeAlMenos32Bytes!";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Generar token con rol
    public String generarToken(String correo, String rol) {

        return Jwts.builder()
                .setSubject(correo)
                .claim("rol", rol)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtener correo del token
    public String obtenerCorreo(String token) {
        return obtenerClaims(token).getSubject();
    }

    // Obtener rol
    public String obtenerRol(String token){
        return obtenerClaims(token).get("rol", String.class);
    }

    // Obtener claims
    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validar token
    public boolean validarToken(String token, String correo) {

        String correoToken = obtenerCorreo(token);

        return correoToken.equals(correo) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token){
        return obtenerClaims(token).getExpiration().before(new Date());
    }
}
