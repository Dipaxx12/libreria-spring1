package com.distribuida.controller;

import com.distribuida.model.Categoria;
import com.distribuida.service.CategoriaService;
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

class CategoriaControllerTestUnitaria {

    @InjectMocks
    private CategoriaController categoriaController;

    @Mock
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setCategoria("Terror");
        categoria.setDescripcion("Historias de terror");
    }

    @Test
    void testFindAll() {
        when(categoriaService.findAll()).thenReturn(List.of(categoria));

        ResponseEntity<List<Categoria>> response = categoriaController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(categoriaService, times(1)).findAll();
    }

    @Test
    void testFinOne() {
        when(categoriaService.findOne(1)).thenReturn(categoria);

        ResponseEntity<Categoria> response = categoriaController.findOne(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Terror", response.getBody().getCategoria());
    }

    @Test
    void testFindOneNoExistente() {
        when(categoriaService.findOne(2)).thenReturn(null);

        ResponseEntity<Categoria> response = categoriaController.findOne(2);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testSave() {
        when(categoriaService.save(categoria)).thenReturn(categoria);

        ResponseEntity<Categoria> response = categoriaController.save(categoria);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Terror", response.getBody().getCategoria());
    }

    @Test
    void testUpdate() {
        when(categoriaService.update(1, categoria)).thenReturn(categoria);

        ResponseEntity<Categoria> response = categoriaController.update(1, categoria);

        assertEquals(200, response.getStatusCodeValue());
        verify(categoriaService).update(1, categoria);
    }

    @Test
    void testUpdateNoExistente() {
        when(categoriaService.update(eq(2), any(Categoria.class))).thenReturn(null);

        ResponseEntity<Categoria> response = categoriaController.update(2, categoria);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        doNothing().when(categoriaService).delete(1);

        ResponseEntity<Void> response = categoriaController.delete(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(categoriaService).delete(1);
    }
}
