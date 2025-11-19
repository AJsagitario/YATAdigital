package com.gobilletera.wallet_core.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private static final String CENTRAL_TOKEN = "sk_yata_b7c8d9e0f1g2h3i4";

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
            String contactoDestino = req.getDestino();
            BigDecimal monto = BigDecimal.valueOf(req.getMonto());

            // Validar saldo y existencia en YaTa
            Usuario origen = usuarioRepository.findById(dniOrigen).orElse(null);
            if (origen == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Usuario origen no encontrado"));
            }
            // 2) Validar PIN
            if (req.getPin() == null || !req.getPin().equals(origen.getPin())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "PIN incorrecto"));
            }
            if (origen.getSaldo().compareTo(monto) < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Saldo insuficiente"));
            }

            // Construir request hacia la API central (formato del README)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-Token", CENTRAL_TOKEN);

            Map<String, Object> payload = new HashMap<>();
            payload.put("fromIdentifier", origen.getContacto()); // número del emisor
            payload.put("toIdentifier", contactoDestino); // número destino
            payload.put("toAppName", req.getToAppName()); // ej. "PlataYa"
            payload.put("monto", req.getMonto());
            payload.put("descripcion",
                    req.getMensaje() != null ? req.getMensaje() : "Transferencia desde YaTa");

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            // Enviar solicitud a la API central
            ResponseEntity<String> respuestaCentral = restTemplate.postForEntity(
                    CENTRAL_API_URL, requestEntity, String.class);

            // Si la API central no responde 2xx, devolvemos error genérico
            if (!respuestaCentral.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(502).body(Map.of("error", "API central rechazó la operación",
                        "detalle", respuestaCentral.getBody()));
            }

            // Descontar saldo local y registrar movimiento
            origen.setSaldo(origen.getSaldo().subtract(monto));
            usuarioRepository.save(origen);

            Movimiento mov = new Movimiento();
            mov.setId(UUID.randomUUID());
            mov.setDniOrigen(dniOrigen);
            mov.setDniDestino(contactoDestino); // aquí puedes guardar el número destino
            mov.setMonto(monto);
            mov.setMensaje(req.getMensaje() != null ? req.getMensaje() : "Transferencia externa");
            mov.setCodigoTransferencia(UUID.randomUUID().toString().substring(0, 8));
            mov.setFechaHora(LocalDateTime.now());
            movimientoRepository.save(mov);

            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "msg", "Transferencia externa enviada correctamente",
                    "respuestaCentral", respuestaCentral.getBody()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
