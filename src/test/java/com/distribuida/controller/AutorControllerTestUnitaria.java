package com.distribuida.controller;

import com.distribuida.model.Autor;
import com.distribuida.service.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AutorControllerTestUnitaria {

    @InjectMocks
    private AutorController autorController;

    @Mock
    private AutorService autorService;

    private Autor autor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        autor = new Autor();
        autor.setIdAutor(1);
        autor.setNombre("Gabriel");
        autor.setApellido("García Márquez");
        autor.setPais("Colombia");
        autor.setDireccion("Aracataca");
        autor.setTelefono("0999999999");
        autor.setCorreo("gabriel@example.com");
    }

    @Test
    void testFindAll() {
        when(autorService.findAll()).thenReturn(List.of(autor));

        ResponseEntity<List<Autor>> response = autorController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(autorService, times(1)).findAll();
    }

    @Test
    void testFinOne() {
        when(autorService.findOne(1)).thenReturn(autor);

        ResponseEntity<Autor> response = autorController.findOne(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Gabriel", response.getBody().getNombre());
    }

    @Test
    void testFindOneNoExistente() {
        when(autorService.findOne(2)).thenReturn(null);

        ResponseEntity<Autor> response = autorController.findOne(2);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testSave() {
        when(autorService.save(autor)).thenReturn(autor);

        ResponseEntity<Autor> response = autorController.save(autor);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Gabriel", response.getBody().getNombre());
    }

    @Test
    void testUpdate() {
        when(autorService.update(1, autor)).thenReturn(autor);

        ResponseEntity<Autor> response = autorController.update(1, autor);

        assertEquals(200, response.getStatusCodeValue());
        verify(autorService).update(1, autor);
    }

    @Test
    void testUpdateNoExistente() {
        when(autorService.update(eq(2), any(Autor.class))).thenReturn(null);

        ResponseEntity<Autor> response = autorController.update(2, autor);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        doNothing().when(autorService).delete(1);

        ResponseEntity<Void> response = autorController.delete(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(autorService, times(1)).delete(1);
    }
}
