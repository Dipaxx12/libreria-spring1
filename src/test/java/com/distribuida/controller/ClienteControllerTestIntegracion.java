package com.distribuida.controller;

import com.distribuida.model.Cliente;
import com.distribuida.service.ClienteService;
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

@WebMvcTest(ClienteController.class)
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean                    //  Marca deprecated → solo warning; funcional
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    /* ---------- util ---------- */
    private Cliente mockCliente() {
        Cliente c = new Cliente();
        c.setIdCliente(1);
        c.setCedula("1755564901");
        c.setNombre("Pedro");
        c.setApellido("Hidalgo");
        c.setDireccion("Puembo");
        c.setTelefono("0979272218");
        c.setCorreo("pedro@example.com");
        return c;
    }

    /* ---------- tests ---------- */

    @Test
    void testFindAll() throws Exception {
        Mockito.when(clienteService.findAll()).thenReturn(List.of(mockCliente()));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Pedro")));
    }

    @Test
    void testFinOne() throws Exception {
        Mockito.when(clienteService.findOne(1)).thenReturn(mockCliente());

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Pedro")));
    }

    @Test
    void testFindOneNoExistente() throws Exception {
        Mockito.when(clienteService.findOne(2)).thenReturn(null);

        mockMvc.perform(get("/api/clientes/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSave() throws Exception {
        Cliente nuevo = mockCliente();
        Mockito.when(clienteService.save(any(Cliente.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Pedro")));
    }

    @Test
    void testUpdate() throws Exception {
        Cliente actualizado = mockCliente();
        actualizado.setNombre("Pedro Modificado");

        Mockito.when(clienteService.update(eq(1), any(Cliente.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Pedro Modificado")));
    }

    @Test
    void testUpdateNoExistente() throws Exception {
        Mockito.when(clienteService.update(eq(2), any(Cliente.class))).thenReturn(null);

        mockMvc.perform(put("/api/clientes/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCliente())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(clienteService).delete(1);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }
}
