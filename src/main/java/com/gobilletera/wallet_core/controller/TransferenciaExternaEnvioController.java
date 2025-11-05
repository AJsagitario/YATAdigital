package com.gobilletera.wallet_core.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.gobilletera.wallet_core.model.Movimiento;
import com.gobilletera.wallet_core.model.TransferenciaRequest;
import com.gobilletera.wallet_core.model.Usuario;
import com.gobilletera.wallet_core.repository.MovimientoRepository;
import com.gobilletera.wallet_core.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/externo")
@CrossOrigin("*")
public class TransferenciaExternaEnvioController {
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

            // validar saldo/existencia
            Usuario origen = usuarioRepository.findById(dniOrigen).orElse(null);
            if (origen == null || origen.getSaldo().compareTo(monto) < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Saldo insuficiente o usuario no encontrado"));
            }
            // Llamar a la API Central
            String apiCentralUrl = "https://billetera-central-api.onrender.com/api/transfer";

            Map<String, Object> payload = new HashMap<>();
            payload.put("app_name", "YaTa");
            payload.put("token", "en espera");
            payload.put("dniOrigen", dniOrigen);
            payload.put("dniDestino", dniDestino);
            payload.put("monto", req.getMonto());

            ResponseEntity<String> respuestaCentral = restTemplate.postForEntity(apiCentralUrl, payload, String.class);

            // Si fue exitosa, resta saldo y guarda movimiento
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
            mov.setMensaje(req.getMensaje() != null ? req.getMensaje() : "Transferencia exitosa");
            mov.setCodigoTransferencia(UUID.randomUUID().toString().substring(0, 8));
            mov.setFechaHora(LocalDateTime.now());
            movimientoRepository.save(mov);

            return ResponseEntity.ok(Map.of("status", "ok"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }

    }
}
