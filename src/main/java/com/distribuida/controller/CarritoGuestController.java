package com.distribuida.controller;

import com.distribuida.model.Carrito;
import com.distribuida.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/guest/cart")
public class CarritoGuestController {

    private final CarritoService carritoService;

    public CarritoGuestController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping
    public ResponseEntity<Carrito> createOrGet(@RequestParam("token") String token) {
        return ResponseEntity.ok(carritoService.getOrCreateByToken(token));
    }

    @GetMapping
    public ResponseEntity<Carrito> get(@RequestParam("token") String token) {
        return ResponseEntity.ok(carritoService.getByToken(token));
    }

    @PostMapping("/items")
    public ResponseEntity<Carrito> addItem(@RequestParam("token") String token,
            @RequestBody Map<String, Integer> body) {
        int libroId = body.getOrDefault("libroId", 0);
        int cantidad = body.getOrDefault("cantidad", 0);
        return ResponseEntity.ok(carritoService.addItem(token, libroId, cantidad));
    }

    @PutMapping("/items/{carritoItemId}")
    public ResponseEntity<Carrito> update(@RequestParam("token") String token,
            @PathVariable long carritoItemId,
            @RequestBody Map<String, Integer> body) {
        int cantidad = body.getOrDefault("cantidad", 0);
        return ResponseEntity.ok(carritoService.updateItemCantidad(token, carritoItemId, cantidad));
    }

    @DeleteMapping("/items/{carritoItemId}")
    public ResponseEntity<Void> remove(@RequestParam("token") String token,
            @PathVariable long carritoItemId) {
        carritoService.removeItem(token, carritoItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clear(@RequestParam("token") String token) {
        carritoService.clearByToken(token);
        return ResponseEntity.noContent().build();
    }
}
