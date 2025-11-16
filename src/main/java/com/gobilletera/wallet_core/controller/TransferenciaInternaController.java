package com.gobilletera.wallet_core.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gobilletera.wallet_core.model.Movimiento;
import com.gobilletera.wallet_core.model.TransferenciaRequest;
import com.gobilletera.wallet_core.model.Usuario;
import com.gobilletera.wallet_core.repository.MovimientoRepository;
import com.gobilletera.wallet_core.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/transferencias")
@CrossOrigin("*")
public class TransferenciaInternaController {

    private final UsuarioRepository usuarioRepository;
    private final MovimientoRepository movimientoRepository;

    public TransferenciaInternaController(UsuarioRepository usuarioRepository,
                                          MovimientoRepository movimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @PostMapping("/interna")
    public ResponseEntity<?> transferirInterna(@RequestBody TransferenciaRequest req) {
        try {
            String dniOrigen = req.getOrigen();
            String dniDestino = req.getDestino();
            BigDecimal monto = BigDecimal.valueOf(req.getMonto());

            // 1. Validar existencia de origen y destino
            Usuario origen = usuarioRepository.findById(dniOrigen).orElse(null);
            Usuario destino = usuarioRepository.findById(dniDestino).orElse(null);

            if (origen == null || destino == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Usuario origen o destino no existe"));
            }

            // 2. Validar saldo
            if (origen.getSaldo().compareTo(monto) < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Saldo insuficiente en la cuenta origen"));
            }

            // 3. Actualizar saldos
            origen.setSaldo(origen.getSaldo().subtract(monto));
            destino.setSaldo(destino.getSaldo().add(monto));
            usuarioRepository.save(origen);
            usuarioRepository.save(destino);

            // 4. Registrar movimiento
            Movimiento mov = new Movimiento();
            mov.setId(UUID.randomUUID());
            mov.setDniOrigen(dniOrigen);
            mov.setDniDestino(dniDestino);
            mov.setMonto(monto);
            mov.setMensaje(
                    req.getMensaje() != null ? req.getMensaje() : "Transferencia interna");
            mov.setCodigoTransferencia(UUID.randomUUID().toString().substring(0, 8));
            mov.setFechaHora(LocalDateTime.now());
            movimientoRepository.save(mov);

            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "msg", "Transferencia interna realizada correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
