package com.distribuida.controller;

import com.distribuida.model.Factura;
import com.distribuida.model.FacturaDetalle;
import com.distribuida.model.Libro;
import com.distribuida.service.FacturaDetalleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FacturaDetalleControllerTestUnitaria {

    @InjectMocks
    private FacturaDetalleController facturaDetalleController;

    @Mock
    private FacturaDetalleService facturaDetalleService;

    private FacturaDetalle detalle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Libro libro = new Libro();
        libro.setIdLibro(1);
        libro.setTitulo("Cien Años de Soledad");

        Factura factura = new Factura();
        factura.setIdFactura(1);

        detalle = new FacturaDetalle();
        detalle.setIdFacturaDetalle(1);
        detalle.setCantidad(2);
        detalle.setSubTotal(50.0);
        detalle.setFactura(factura);
        detalle.setLibro(libro);
    }

    @Test
    void testFindAll() {
        when(facturaDetalleService.findAll()).thenReturn(List.of(detalle));

        ResponseEntity<List<FacturaDetalle>> response = facturaDetalleController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(facturaDetalleService).findAll();
    }

    @Test
    void testFinOne() {
        when(facturaDetalleService.findOne(1)).thenReturn(detalle);

        ResponseEntity<FacturaDetalle> response = facturaDetalleController.findOne(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getCantidad());
    }

    @Test
    void testFindOneNoExistente() {
        when(facturaDetalleService.findOne(2)).thenReturn(null);

        ResponseEntity<FacturaDetalle> response = facturaDetalleController.findOne(2);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testSave() {
        when(facturaDetalleService.save(detalle)).thenReturn(detalle);

        ResponseEntity<FacturaDetalle> response = facturaDetalleController.save(detalle);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(50.0, response.getBody().getSubTotal());
    }

    @Test
    void testUpdate() {
        when(facturaDetalleService.update(1, detalle)).thenReturn(detalle);

        ResponseEntity<FacturaDetalle> response = facturaDetalleController.update(1, detalle);

        assertEquals(200, response.getStatusCodeValue());
        verify(facturaDetalleService).update(1, detalle);
    }

    @Test
    void testUpdateNoExistente() {
        when(facturaDetalleService.update(eq(2), any(FacturaDetalle.class))).thenReturn(null);

        ResponseEntity<FacturaDetalle> response = facturaDetalleController.update(2, detalle);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        doNothing().when(facturaDetalleService).delete(1);

        ResponseEntity<Void> response = facturaDetalleController.delete(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(facturaDetalleService).delete(1);
    }
}
