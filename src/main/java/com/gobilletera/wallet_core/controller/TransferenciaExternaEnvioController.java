package com.gobilletera.wallet_core.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;

import com.gobilletera.wallet_core.model.Movimiento;
import com.gobilletera.wallet_core.model.TransferenciaRequest;
import com.gobilletera.wallet_core.model.Usuario;
import com.gobilletera.wallet_core.repository.MovimientoRepository;
import com.gobilletera.wallet_core.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/externo")
@CrossOrigin("*")
public class TransferenciaExternaEnvioController {

    private static final String CENTRAL_API_URL = "https://billetera-central-api.onrender.com/api/v1/transfer";
    private static final String CENTRAL_TOKEN = "token";
    private static final String APP_NAME = "YaTa";

    private final UsuarioRepository usuarioRepository;
    private final MovimientoRepository movimientoRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferenciaExternaEnvioController(UsuarioRepository usuarioRepository,
            MovimientoRepository movimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @PutMapping("/transferir")
    public ResponseEntity<?> transferirExterno(@RequestBody TransferenciaRequest req) {
        try {
            String dniOrigen = req.getOrigen();
            String dniDestino = req.getDestino();
            BigDecimal monto = BigDecimal.valueOf(req.getMonto());

            // Validar saldo y existencia
            Usuario origen = usuarioRepository.findById(dniOrigen).orElse(null);
            if (origen == null || origen.getSaldo().compareTo(monto) < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Saldo insuficiente o usuario no encontrado"));
            }

            // Construir request hacia la API central con el formato correcto del README
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-Token", CENTRAL_TOKEN);

            Map<String, Object> payload = new HashMap<>();
            payload.put("fromIdentifier", dniOrigen);
            payload.put("toIdentifier", dniDestino);
            payload.put("toAppName", APP_NAME);
            payload.put("monto", req.getMonto());
            payload.put("descripcion", req.getMensaje() != null ? req.getMensaje() : "Transferencia desde YaTa");

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            // Enviar solicitud a la API central
            ResponseEntity<String> respuestaCentral = restTemplate.postForEntity(
                    CENTRAL_API_URL, requestEntity, String.class);

            // Si fue exitosa, actualizamos saldo y guardamos el movimiento
            if (!respuestaCentral.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(502).body(Map.of("error", "API central rechazó la operación"));
            }

            origen.setSaldo(origen.getSaldo().subtract(monto));
            usuarioRepository.save(origen);

            Movimiento mov = new Movimiento();
            mov.setId(UUID.randomUUID());
            mov.setDniOrigen(dniOrigen);
            mov.setDniDestino(dniDestino);
            mov.setMonto(monto);
            mov.setMensaje(req.getMensaje() != null ? req.getMensaje() : "Transferencia externa exitosa");
            mov.setCodigoTransferencia(UUID.randomUUID().toString().substring(0, 8));
            mov.setFechaHora(LocalDateTime.now());
            movimientoRepository.save(mov);

            return ResponseEntity.ok(Map.of("status", "ok", "msg", "Transferencia enviada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
