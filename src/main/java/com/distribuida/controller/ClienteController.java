package com.distribuida.controller;


import com.distribuida.model.Cliente;
import com.distribuida.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        List<Cliente> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findOne(@PathVariable int id) {
        Cliente cliente = clienteService.findOne(id);
        if(cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente) {
        Cliente cliente1 = clienteService.save(cliente);
        return ResponseEntity.ok(cliente1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable int id,
                                          @RequestBody Cliente cliente) {
        Cliente actualizado = clienteService.update(id, cliente);
        return (actualizado != null)
                ? ResponseEntity.ok(actualizado)
                : ResponseEntity.notFound().build();   // ← devuelve 404 si no existe
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
