package com.distribuida.controller;

import com.distribuida.model.Libro;
import com.distribuida.service.LibroService;
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

class LibroControllerTestUnitaria {

    @InjectMocks
    private LibroController libroController;

    @Mock
    private LibroService libroService;

    private Libro libro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        libro = new Libro();
        libro.setIdLibro(1);
        libro.setTitulo("Cien Años de Soledad");
        libro.setEditorial("Sudamericana");
        libro.setNumPaginas(471);
        libro.setEdicion("1ra");
        libro.setIdioma("Español");
        libro.setFechaPublicacion(new Date());
        libro.setDescripcion("Novela icónica del realismo mágico");
        libro.setTipoPasta("Dura");
        libro.setIsbn("978-0307474728");
        libro.setNumEjemplares(10);
        libro.setPortada("portada.jpg");
        libro.setPresentacion("Tapa dura");
        libro.setPrecio(25.99);
    }

    @Test
    void testFindAll() {
        when(libroService.findAll()).thenReturn(List.of(libro));

        ResponseEntity<List<Libro>> response = libroController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(libroService, times(1)).findAll();
    }

    @Test
    void testFinOne() {
        when(libroService.findOne(1)).thenReturn(libro);

        ResponseEntity<Libro> response = libroController.findOne(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cien Años de Soledad", response.getBody().getTitulo());
    }

    @Test
    void testFindOneNoExistente() {
        when(libroService.findOne(2)).thenReturn(null);

        ResponseEntity<Libro> response = libroController.findOne(2);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testSave() {
        when(libroService.save(libro)).thenReturn(libro);

        ResponseEntity<Libro> response = libroController.save(libro);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cien Años de Soledad", response.getBody().getTitulo());
    }

    @Test
    void testUpdate() {
        when(libroService.update(1, libro)).thenReturn(libro);

        ResponseEntity<Libro> response = libroController.update(1, libro);

        assertEquals(200, response.getStatusCodeValue());
        verify(libroService).update(1, libro);
    }

    @Test
    void testUpdateNoExistente() {
        when(libroService.update(eq(2), any(Libro.class))).thenReturn(null);

        ResponseEntity<Libro> response = libroController.update(2, libro);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        doNothing().when(libroService).delete(1);

        ResponseEntity<Void> response = libroController.delete(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(libroService).delete(1);
    }
}
