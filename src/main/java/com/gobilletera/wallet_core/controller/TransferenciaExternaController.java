package com.gobilletera.wallet_core.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobilletera.wallet_core.model.Movimiento;
import com.gobilletera.wallet_core.model.Usuario;
import com.gobilletera.wallet_core.repository.MovimientoRepository;
import com.gobilletera.wallet_core.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/externo")
public class TransferenciaExternaController {

    private final UsuarioRepository usuarioRepository;
    private final MovimientoRepository movimientoRepository;

    public TransferenciaExternaController(UsuarioRepository usuarioRepository,
            MovimientoRepository movimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @PostMapping("/depositar")
    public ResponseEntity<?> recibirTransferenciaExterna(@RequestBody Map<String, Object> payload) {
        try {
            // lo que la API central nos envía
            String dniDestino = (String) payload.get("dniDestino");
            Double monto = Double.valueOf(payload.get("monto").toString());
            String appOrigen = (String) payload.get("app_name");

            // validacion si el usuario exisste
            Usuario usuario = usuarioRepository.findById(dniDestino).orElse(null);
            if (usuario == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }

            // actualiar saldo
            usuario.setSaldo(usuario.getSaldo().add(BigDecimal.valueOf(monto)));
            usuarioRepository.save(usuario);

            // registrar movimiento en nuestro billetera
            Movimiento mov = new Movimiento();
            mov.setId(UUID.randomUUID());
            mov.setDniOrigen(appOrigen);
            mov.setDniDestino(dniDestino);
            mov.setMonto(BigDecimal.valueOf(monto));
            mov.setMensaje("DEPÓSITO RECIBIDO DE " + appOrigen);
            mov.setCodigoTransferencia(UUID.randomUUID().toString().substring(0, 8));
            mov.setFechaHora(LocalDateTime.now());
            movimientoRepository.save(mov);

            return ResponseEntity.ok(Map.of("status", "ok", "msg", "Transferencia recibida en YaTa"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }

    }
}
