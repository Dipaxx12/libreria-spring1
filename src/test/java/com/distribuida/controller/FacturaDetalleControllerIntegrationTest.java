package com.distribuida.controller;

import com.distribuida.model.Factura;
import com.distribuida.model.FacturaDetalle;
import com.distribuida.model.Libro;
import com.distribuida.service.FacturaDetalleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacturaDetalleController.class)
class FacturaDetalleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacturaDetalleService facturaDetalleService;

    @Autowired
    private ObjectMapper objectMapper;

    /* ---------- util ---------- */
    private FacturaDetalle mockDetalle() {
        Libro libro = new Libro();
        libro.setIdLibro(1);
        libro.setTitulo("Cien Años de Soledad");

        Factura factura = new Factura();
        factura.setIdFactura(1);

        FacturaDetalle d = new FacturaDetalle();
        d.setIdFacturaDetalle(1);
        d.setCantidad(2);
        d.setSubTotal(50.0);
        d.setFactura(factura);
        d.setLibro(libro);
        return d;
    }

    /* ---------- tests ---------- */

    @Test
    void testFindAll() throws Exception {
        Mockito.when(facturaDetalleService.findAll()).thenReturn(List.of(mockDetalle()));

        mockMvc.perform(get("/api/facturas-detalle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cantidad", is(2)));
    }

    @Test
    void testFinOne() throws Exception {
        Mockito.when(facturaDetalleService.findOne(3)).thenReturn(mockDetalle());

        mockMvc.perform(get("/api/facturas-detalle/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subTotal", is(50.0)));
    }

    @Test
    void testFindOneNoExistente() throws Exception {
        Mockito.when(facturaDetalleService.findOne(2)).thenReturn(null);

        mockMvc.perform(get("/api/facturas-detalle/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSave() throws Exception {
        FacturaDetalle nuevo = mockDetalle();
        Mockito.when(facturaDetalleService.save(any(FacturaDetalle.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/facturas-detalle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad", is(2)));
    }

    @Test
    void testUpdate() throws Exception {
        FacturaDetalle actualizado = mockDetalle();
        actualizado.setCantidad(3);

        Mockito.when(facturaDetalleService.update(eq(1), any(FacturaDetalle.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/facturas-detalle/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad", is(3)));
    }

    @Test
    void testUpdateNoExistente() throws Exception {
        Mockito.when(facturaDetalleService.update(eq(2), any(FacturaDetalle.class))).thenReturn(null);

        mockMvc.perform(put("/api/facturas-detalle/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockDetalle())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(facturaDetalleService).delete(1);

        mockMvc.perform(delete("/api/facturas-detalle/1"))
                .andExpect(status().isNoContent());
    }
}
