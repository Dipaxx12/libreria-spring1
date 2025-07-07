package com.distribuida.controller;

import com.distribuida.model.Libro;
import com.distribuida.service.LibroService;
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

@WebMvcTest(LibroController.class)
class LibroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroService;

    @Autowired
    private ObjectMapper objectMapper;

    /* ---------- util ---------- */
    private Libro mockLibro() {
        Libro l = new Libro();
        l.setIdLibro(1);
        l.setTitulo("Cien Años de Soledad");
        l.setEditorial("Sudamericana");
        l.setNumPaginas(471);
        l.setEdicion("1ra");
        l.setIdioma("Español");
        l.setFechaPublicacion(new Date());
        l.setDescripcion("Novela icónica del realismo mágico");
        l.setTipoPasta("Dura");
        l.setIsbn("978-0307474728");
        l.setNumEjemplares(10);
        l.setPortada("portada.jpg");
        l.setPresentacion("Tapa dura");
        l.setPrecio(25.99);
        return l;
    }

    /* ---------- tests ---------- */

    @Test
    void testFindAll() throws Exception {
        Mockito.when(libroService.findAll()).thenReturn(List.of(mockLibro()));

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Cien Años de Soledad")));
    }

    @Test
    void testFinOne() throws Exception {
        Mockito.when(libroService.findOne(1)).thenReturn(mockLibro());

        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.editorial", is("Sudamericana")));
    }

    @Test
    void testFindOneNoExistente() throws Exception {
        Mockito.when(libroService.findOne(2)).thenReturn(null);

        mockMvc.perform(get("/api/libros/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSave() throws Exception {
        Libro nuevo = mockLibro();
        Mockito.when(libroService.save(any(Libro.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Cien Años de Soledad")));
    }

    @Test
    void testUpdate() throws Exception {
        Libro actualizado = mockLibro();
        actualizado.setPrecio(29.99);

        Mockito.when(libroService.update(eq(1), any(Libro.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precio", is(29.99)));
    }

    @Test
    void testUpdateNoExistente() throws Exception {
        Mockito.when(libroService.update(eq(2), any(Libro.class))).thenReturn(null);

        mockMvc.perform(put("/api/libros/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockLibro())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(libroService).delete(1);

        mockMvc.perform(delete("/api/libros/1"))
                .andExpect(status().isNoContent());
    }
}
