package com.distribuida.controller;

import com.distribuida.model.FacturaDetalle;
import com.distribuida.service.FacturaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/facturas-detalle")
@RequiredArgsConstructor
public class FacturaDetalleController {

    private final FacturaDetalleService facturaDetalleService;

    @GetMapping
    public ResponseEntity<List<FacturaDetalle>> findAll() {
        return ResponseEntity.ok(facturaDetalleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDetalle> findOne(@PathVariable int id) {
        FacturaDetalle detalle = facturaDetalleService.findOne(id);
        return (detalle != null)
                ? ResponseEntity.ok(detalle)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FacturaDetalle> save(@RequestBody FacturaDetalle facturaDetalle) {
        return ResponseEntity.ok(facturaDetalleService.save(facturaDetalle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaDetalle> update(@PathVariable int id, @RequestBody FacturaDetalle facturaDetalle) {
        FacturaDetalle actualizado = facturaDetalleService.update(id, facturaDetalle);
        return (actualizado != null)
                ? ResponseEntity.ok(actualizado)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        facturaDetalleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
