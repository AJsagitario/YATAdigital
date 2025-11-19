package com.gobilletera.wallet_core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gobilletera.wallet_core.model.Usuario;
import com.gobilletera.wallet_core.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // 1) login por DNI (el que ya tenías)
    @GetMapping("/login/contacto/{contacto}/{pin}")
    public ResponseEntity<?> loginPorContactoYPin(
            @PathVariable String contacto,
            @PathVariable String pin) {

        Usuario usuario = usuarioRepository.findByContactoAndPin(contacto, pin);
        if (usuario == null) {
            return ResponseEntity.status(401).body(
                    java.util.Map.of("error", "Número o PIN incorrecto"));
        }
        return ResponseEntity.ok(usuario);
    }
}

