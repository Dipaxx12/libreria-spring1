package com.distribuida.controller;

import com.distribuida.model.Factura;
import com.distribuida.service.GuestCheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guest/checkout")
public class GuestCheckoutController {

    private final GuestCheckoutService guestCheckoutService;

    public GuestCheckoutController(GuestCheckoutService service) {
        this.guestCheckoutService = service;
    }

    @PostMapping
    public ResponseEntity<Factura> checkout(@RequestParam String token) {
        return ResponseEntity.ok(guestCheckoutService.checkoutByToken(token));
    }
}
