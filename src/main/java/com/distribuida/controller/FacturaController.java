package com.distribuida.controller;

import com.distribuida.model.Factura;
import com.distribuida.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<Factura>> findAll() {
        return ResponseEntity.ok(facturaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> findOne(@PathVariable int id) {
        Factura factura = facturaService.findOne(id);
        return (factura != null)
                ? ResponseEntity.ok(factura)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Factura> save(@RequestBody Factura factura) {
        return ResponseEntity.ok(facturaService.save(factura));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Factura> update(@PathVariable int id, @RequestBody Factura factura) {
        Factura actualizada = facturaService.update(id, factura);
        return (actualizada != null)
                ? ResponseEntity.ok(actualizada)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        facturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
