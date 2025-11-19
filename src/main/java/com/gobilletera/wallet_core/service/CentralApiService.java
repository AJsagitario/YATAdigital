package com.gobilletera.wallet_core.service;

import com.gobilletera.wallet_core.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CentralApiService {
    @Value("${central.api.base-url}")
    private String baseUrl;

    @Value("${central.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-Token", apiToken);
        return headers;
    }

    // A) Registrar wallet en el directorio central
    public void registrarWallet(Usuario usuario) {
        String url = baseUrl + "/register-wallet";

        Map<String, Object> body = new HashMap<>();
        body.put("userIdentifier", usuario.getContacto()); // teléfono
        body.put("internalWalletId", usuario.getDni()); // tu PK interno
        body.put("userName", usuario.getNombre());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, buildHeaders());

        restTemplate.postForEntity(url, entity, String.class);
    }

    // B) (Opcional) Buscar billeteras para un contacto
    public String buscarWallets(String identifier) {
        String url = baseUrl + "/wallets/" + identifier;
        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    // C) (Opcional) Transferir, si luego quieres centralizar aquí tu lógica
    public String transferir(String fromIdentifier, String toIdentifier,
            String toAppName, double monto, String descripcion) {

        String url = baseUrl + "/transfer";

        Map<String, Object> body = new HashMap<>();
        body.put("fromIdentifier", fromIdentifier);
        body.put("toIdentifier", toIdentifier);
        body.put("toAppName", toAppName);
        body.put("monto", monto);
        body.put("descripcion",
                descripcion != null ? descripcion : "Transferencia desde YaTa");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, buildHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response.getBody();
    }

}
