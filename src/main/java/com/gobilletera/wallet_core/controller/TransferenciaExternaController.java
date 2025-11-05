package com.gobilletera.wallet_core.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gobilletera.wallet_core.model.Movimiento;
import com.gobilletera.wallet_core.model.Usuario;
import com.gobilletera.wallet_core.repository.MovimientoRepository;
import com.gobilletera.wallet_core.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/externo")
@CrossOrigin("*")
public class TransferenciaExternaController {

    private static final String CENTRAL_TOKEN = "token";

    private final UsuarioRepository usuarioRepository;
    private final MovimientoRepository movimientoRepository;

    public TransferenciaExternaController(UsuarioRepository usuarioRepository,
            MovimientoRepository movimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @PostMapping("/depositar")
    public ResponseEntity<?> recibirTransferenciaExterna(
            @RequestHeader(name = "X-API-Token", required = false) String token,
            @RequestBody Map<String, Object> payload) {

        try {
            // validar token
            if (token == null || !token.equals(CENTRAL_TOKEN)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "msg", "token inválido"));
            }

            // leer lo que manda la API central
            String walletId = (String) payload.get("internalWalletId"); // en tu caso es el DNI
            String fromAppName = (String) payload.get("fromAppName");
            Double monto = Double.valueOf(payload.get("monto").toString());
            String descripcion = (String) payload.getOrDefault("descripcion", "Depósito externo");
            String centralTxId = (String) payload.get("centralTransactionId");

            // buscar al usuario (tu llave es el DNI)
            Usuario usuario = usuarioRepository.findById(walletId).orElse(null);
            if (usuario == null) {
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "msg", "usuario no encontrado"));
            }

            // actualizar saldo
            usuario.setSaldo(
                    usuario.getSaldo().add(BigDecimal.valueOf(monto)));
            usuarioRepository.save(usuario);

            // registrar movimiento
            Movimiento mov = new Movimiento();
            mov.setId(UUID.randomUUID());

            mov.setDniOrigen(fromAppName);
            mov.setDniDestino(walletId);
            mov.setMonto(BigDecimal.valueOf(monto));
            mov.setMensaje(descripcion + " desde " + fromAppName);
            mov.setCodigoTransferencia(
                    centralTxId != null ? centralTxId : UUID.randomUUID().toString().substring(0, 8));
            mov.setFechaHora(LocalDateTime.now());
            movimientoRepository.save(mov);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "localTransactionId", mov.getId().toString()));

        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "success", false,
                    "msg", e.getMessage()));
        }
    }
}
