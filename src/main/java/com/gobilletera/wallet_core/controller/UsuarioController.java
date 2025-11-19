package com.gobilletera.wallet_core.controller;

import com.gobilletera.wallet_core.model.Movimiento;
import com.gobilletera.wallet_core.model.TransferenciaRequest;
import com.gobilletera.wallet_core.model.Usuario;
import com.gobilletera.wallet_core.repository.MovimientoRepository;
import com.gobilletera.wallet_core.repository.UsuarioRepository;
import com.gobilletera.wallet_core.service.CentralApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MovimientoRepository movimientoRepository;
    @Autowired
    private CentralApiService centralApiService;

    // crear usuario
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuarioRequest) {
        if (usuarioRepository.existsById(usuarioRequest.getDni())) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "Ya existe un usuario con ese DNI");
            errorBody.put("codigo", "USER_ALREADY_EXISTS");
            errorBody.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(errorBody);
        }

        // este es el que tenías
        Usuario nuevoUsuario = new Usuario(usuarioRequest.getDni(), usuarioRequest.getNombre());

        // 👇 seteamos el contacto que vino en el JSON
        nuevoUsuario.setContacto(usuarioRequest.getContacto());

        // 👇 NUEVO: seteamos el pin que viene del frontend
        nuevoUsuario.setPin(usuarioRequest.getPin());

        // si quisieras también permitir que mande saldo inicial desde el body:
        if (usuarioRequest.getSaldo() != null) {
            nuevoUsuario.setSaldo(usuarioRequest.getSaldo());
        }

        usuarioRepository.save(nuevoUsuario);
        // rEGISTRO EN API CENTRAL
        try {
            centralApiService.registrarWallet(nuevoUsuario);
        } catch (Exception e) {
            System.out.println("⚠️ Error registrando en API central: " + e.getMessage());
            // no disparas error al frontend; solo logueas
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // listar todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // buscar usuario por su dni
    @GetMapping("/{dni}")
    public ResponseEntity<Usuario> obtenerPorDni(@PathVariable String dni) {
        Optional<Usuario> usuario = usuarioRepository.findById(dni);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaRequest request) {
        // validar que no se transfiera a si mismo
        if (request.getOrigen().equals(request.getDestino())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No puedes transferirte a ti mismo"));
        }
        // validar existencia de usuarios
        Optional<Usuario> emisorOpt = usuarioRepository.findById(request.getOrigen());
        Optional<Usuario> receptorOpt = usuarioRepository.findById(request.getDestino());

        if (emisorOpt.isEmpty() || receptorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Uno o ambos DNIs no existen"));
        }

        Usuario emisor = emisorOpt.get();
        Usuario receptor = receptorOpt.get();

        // 🔐 NUEVO: validar PIN
        if (request.getPin() == null || !request.getPin().equals(emisor.getPin())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "PIN incorrecto"));
        }

        BigDecimal monto = BigDecimal.valueOf(request.getMonto());
        // validar monto minimo
        if (monto.compareTo(BigDecimal.valueOf(1.00)) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El monto minimo para transferir es de 1.00"));
        }
        // validar monto maximo
        if (monto.compareTo(BigDecimal.valueOf(300.00)) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El monto máximo por transferencia es S/.300.00"));
        }

        // VALIDAR SALDO SUFICIENTE
        if (emisor.getSaldo().compareTo(monto) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Saldo insuficiente para realizar la transferencia"));
        }

        // validar longitud del mensaje
        if (request.getMensaje() != null && request.getMensaje().length() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El mensaje no puede superar los 100 caracteres"));
        }

        // realizar transferencia
        emisor.setSaldo(emisor.getSaldo().subtract(BigDecimal.valueOf(request.getMonto())));
        receptor.setSaldo(receptor.getSaldo().add(BigDecimal.valueOf(request.getMonto())));

        usuarioRepository.save(emisor);
        usuarioRepository.save(receptor);

        // generar código de operación aleatorio
        String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // registrar el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setId(UUID.randomUUID());
        movimiento.setDniOrigen(emisor.getDni());
        movimiento.setDniDestino(receptor.getDni());
        movimiento.setMonto(monto);
        movimiento.setMensaje(request.getMensaje());
        movimiento.setCodigoTransferencia(codigo);
        movimiento.setFechaHora(LocalDateTime.now());

        movimientoRepository.save(movimiento);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo_transferencia", codigo);
        respuesta.put("de", emisor.getNombre());
        respuesta.put("a", receptor.getNombre());
        respuesta.put("monto", request.getMonto());
        respuesta.put("mensaje", request.getMensaje());
        respuesta.put("fecha", LocalDateTime.now().toString());

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/movimientos/{dni}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorDni(@PathVariable String dni) {
        List<Movimiento> movimientosOrigen = movimientoRepository.findByDniOrigen(dni);
        List<Movimiento> movimientosDestino = movimientoRepository.findByDniDestino(dni);

        List<Movimiento> todos = new ArrayList<>();
        todos.addAll(movimientosOrigen);
        todos.addAll(movimientosDestino);

        // Puedes ordenar los movimientos por fecha descendente si deseas
        todos.sort(Comparator.comparing(Movimiento::getFechaHora).reversed());

        return ResponseEntity.ok(todos);
    }
}
