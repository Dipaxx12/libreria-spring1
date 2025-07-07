package com.distribuida.controller;

import com.distribuida.model.Categoria;
import com.distribuida.service.CategoriaService;
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

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTestIntegration {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // warning deprecado: solo aviso
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    // util
    private Categoria mockCategoria() {
        Categoria c = new Categoria();
        c.setIdCategoria(1);
        c.setCategoria("Terror");
        c.setDescripcion("Historias de terror");
        return c;
    }

    /* ---------- tests ---------- */

    @Test
    void testFindAll() throws Exception {
        Mockito.when(categoriaService.findAll()).thenReturn(List.of(mockCategoria()));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].categoria", is("Terror")));
    }

    @Test
    void testFinOne() throws Exception {
        Mockito.when(categoriaService.findOne(1)).thenReturn(mockCategoria());

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion", is("Historias de terror")));
    }

    @Test
    void testFindOneNoExistente() throws Exception {
        Mockito.when(categoriaService.findOne(2)).thenReturn(null);

        mockMvc.perform(get("/api/categorias/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSave() throws Exception {
        Categoria nuevo = mockCategoria();
        Mockito.when(categoriaService.save(any(Categoria.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria", is("Terror")));
    }

    @Test
    void testUpdate() throws Exception {
        Categoria actualizado = mockCategoria();
        actualizado.setDescripcion("Nuevas historias de terror");
        Mockito.when(categoriaService.update(eq(1), any(Categoria.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion", is("Nuevas historias de terror")));
    }

    @Test
    void testUpdateNoExistente() throws Exception {
        Mockito.when(categoriaService.update(eq(2), any(Categoria.class))).thenReturn(null);

        mockMvc.perform(put("/api/categorias/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategoria())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(categoriaService).delete(1);

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }
}
