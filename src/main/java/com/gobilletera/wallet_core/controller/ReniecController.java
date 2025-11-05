package com.gobilletera.wallet_core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/reniec")
@CrossOrigin("*")
public class ReniecController {
    private static final String API_URL = "https://api.perudevs.com/api/v1/dni/complete";
    private static final String TOKEN = "cGVydWRldnMucHJvZHVjdGlvbi5maXRjb2RlcnMuNjhlZDI0ZGMyZGZhMTIyNjA3ZTgzZTdk";

    @GetMapping("/{dni}")
    public ResponseEntity<?> obtenerDatosPorDni(@PathVariable String dni) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // construimos la URL con el token real
            String url = API_URL + "?document=" + dni + "&key=" + TOKEN;

            // hacemos la petición GET
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error consultando RENIEC:: " + e.getMessage());
        }
    }
}
