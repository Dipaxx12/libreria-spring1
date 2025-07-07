package com.distribuida.controller;

import com.distribuida.model.Cliente;
import com.distribuida.model.Factura;
import com.distribuida.service.FacturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacturaController.class)
class FacturaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // warning por deprecation; funciona
    private FacturaService facturaService;

    @Autowired
    private ObjectMapper objectMapper;

    /* ---------- util ---------- */
    private Factura mockFactura() {
        Cliente cli = new Cliente();
        cli.setIdCliente(1);

        Factura f = new Factura();
        f.setIdFactura(1);
        f.setNumFactura("FAC-001");
        f.setFecha(new Date());
        f.setTotalNeto(100.0);
        f.setIva(12.0);
        f.setTotal(112.0);
        f.setCliente(cli);
        return f;
    }

    /* ---------- tests ---------- */

    @Test
    void testFindAll() throws Exception {
        Mockito.when(facturaService.findAll()).thenReturn(List.of(mockFactura()));

        mockMvc.perform(get("/api/facturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numFactura", is("FAC-001")));
    }

    @Test
    void testFinOne() throws Exception {
        Mockito.when(facturaService.findOne(1)).thenReturn(mockFactura());

        mockMvc.perform(get("/api/facturas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(112.0)));
    }

    @Test
    void testFindOneNoExistente() throws Exception {
        Mockito.when(facturaService.findOne(2)).thenReturn(null);

        mockMvc.perform(get("/api/facturas/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSave() throws Exception {
        Factura nuevo = mockFactura();
        Mockito.when(facturaService.save(any(Factura.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numFactura", is("FAC-001")));
    }

    @Test
    void testUpdate() throws Exception {
        Factura actualizado = mockFactura();
        actualizado.setIva(15.0);
        actualizado.setTotal(115.0);

        Mockito.when(facturaService.update(eq(1), any(Factura.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/facturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iva", is(15.0)));
    }

    @Test
    void testUpdateNoExistente() throws Exception {
        Mockito.when(facturaService.update(eq(2), any(Factura.class))).thenReturn(null);

        mockMvc.perform(put("/api/facturas/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockFactura())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(facturaService).delete(1);

        mockMvc.perform(delete("/api/facturas/1"))
                .andExpect(status().isNoContent());
    }
}
