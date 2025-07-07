package com.distribuida.controller;

import com.distribuida.model.Cliente;
import com.distribuida.model.Factura;
import com.distribuida.service.FacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FacturaControllerTestUnitaria {

    @InjectMocks
    private FacturaController facturaController;

    @Mock
    private FacturaService facturaService;

    private Factura factura;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Cliente cli = new Cliente();
        cli.setIdCliente(1);

        factura = new Factura();
        factura.setIdFactura(1);
        factura.setNumFactura("FAC-001");
        factura.setFecha(new Date());
        factura.setTotalNeto(100.0);
        factura.setIva(12.0);
        factura.setTotal(112.0);
        factura.setCliente(cli);
    }

    @Test
    void testFindAll() {
        when(facturaService.findAll()).thenReturn(List.of(factura));

        ResponseEntity<List<Factura>> response = facturaController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(facturaService, times(1)).findAll();
    }

    @Test
    void testFinOne() {
        when(facturaService.findOne(1)).thenReturn(factura);

        ResponseEntity<Factura> response = facturaController.findOne(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("FAC-001", response.getBody().getNumFactura());
    }

    @Test
    void testFindOneNoExistente() {
        when(facturaService.findOne(2)).thenReturn(null);

        ResponseEntity<Factura> response = facturaController.findOne(2);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testSave() {
        when(facturaService.save(factura)).thenReturn(factura);

        ResponseEntity<Factura> response = facturaController.save(factura);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(112.0, response.getBody().getTotal());
    }

    @Test
    void testUpdate() {
        when(facturaService.update(1, factura)).thenReturn(factura);

        ResponseEntity<Factura> response = facturaController.update(1, factura);

        assertEquals(200, response.getStatusCodeValue());
        verify(facturaService).update(1, factura);
    }

    @Test
    void testUpdateNoExistente() {
        when(facturaService.update(eq(2), any(Factura.class))).thenReturn(null);

        ResponseEntity<Factura> response = facturaController.update(2, factura);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        doNothing().when(facturaService).delete(1);

        ResponseEntity<Void> response = facturaController.delete(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(facturaService).delete(1);
    }
}
