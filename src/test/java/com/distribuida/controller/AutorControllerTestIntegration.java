package com.distribuida.controller;

import com.distribuida.model.Autor;
import com.distribuida.service.AutorService;
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

@WebMvcTest(AutorController.class)
class AutorControllerTestIntegration {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // warning deprecado: solo informativo
    private AutorService autorService;

    @Autowired
    private ObjectMapper objectMapper;

    // util
    private Autor mockAutor() {
        Autor a = new Autor();
        a.setIdAutor(1);
        a.setNombre("Gabriel");
        a.setApellido("García Márquez");
        a.setPais("Colombia");
        a.setDireccion("Aracataca");
        a.setTelefono("0999999999");
        a.setCorreo("gabriel@example.com");
        return a;
    }

    /* ---------- tests ---------- */

    @Test
    void testFindAll() throws Exception {
        Mockito.when(autorService.findAll()).thenReturn(List.of(mockAutor()));

        mockMvc.perform(get("/api/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Gabriel")));
    }

    @Test
    void testFinOne() throws Exception {
        Mockito.when(autorService.findOne(1)).thenReturn(mockAutor());

        mockMvc.perform(get("/api/autores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido", is("García Márquez")));
    }

    @Test
    void testFindOneNoExistente() throws Exception {
        Mockito.when(autorService.findOne(2)).thenReturn(null);

        mockMvc.perform(get("/api/autores/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSave() throws Exception {
        Autor nuevo = mockAutor();
        Mockito.when(autorService.save(any(Autor.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Gabriel")));
    }

    @Test
    void testUpdate() throws Exception {
        Autor actualizado = mockAutor();
        actualizado.setPais("México");
        Mockito.when(autorService.update(eq(1), any(Autor.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/autores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pais", is("México")));
    }

    @Test
    void testUpdateNoExistente() throws Exception {
        Mockito.when(autorService.update(eq(2), any(Autor.class))).thenReturn(null);

        mockMvc.perform(put("/api/autores/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAutor())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(autorService).delete(1);

        mockMvc.perform(delete("/api/autores/1"))
                .andExpect(status().isNoContent());
    }
}
