package com.gobilletera.wallet_core.controller;

import com.gobilletera.wallet_core.service.CentralApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/externo")
@CrossOrigin("*")
public class TransferenciaConsultaController {
    private final CentralApiService centralApiService;

    public TransferenciaConsultaController(CentralApiService centralApiService) {
        this.centralApiService = centralApiService;
    }

    @GetMapping("/wallets/{identifier}")
    public ResponseEntity<String> buscarWallets(@PathVariable String identifier) {
        String body = centralApiService.buscarWallets(identifier);
        return ResponseEntity.ok(body);
    }
    // ver que billeteras hay registradas para un contacto
}
