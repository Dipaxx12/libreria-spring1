package com.distribuida.dao;

import com.distribuida.model.Libro;
import com.distribuida.model.Autor;
import com.distribuida.model.Categoria;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback(false)
public class LibroRepositoryTestIntegration {

    @Autowired private LibroRepository libroRepository;
    @Autowired private AutorRepository autorRepository;
    @Autowired private CategoriaRepository categoriaRepository;

    @Test
    public void findAll() {
        List<Libro> libros = libroRepository.findAll();
        assertNotNull(libros);
        assertTrue(libros.size() > 0);
        libros.forEach(System.out::println);
    }

    @Test
    public void findOne() {
        Optional<Libro> libro = libroRepository.findById(1);
        assertTrue(libro.isPresent(), "El libro con ID 1 debe existir.");
        System.out.println(libro.get());
    }

    @Test
    public void save() {
        // Crear Autor
        Autor autor = new Autor(0, "Antoine", "de Saint-Exupéry", "Francia", "Paris", "000000000", "autor@correo.com");
        autor = autorRepository.save(autor);

        // Crear Categoria
        Categoria categoria = new Categoria(0, "Ficción", "Literatura juvenil");
        categoria = categoriaRepository.save(categoria);

        // Crear Libro
        Libro libro = new Libro();
        libro.setTitulo("El Principito");
        libro.setEditorial("Planeta");
        libro.setNumPaginas(120);
        libro.setEdicion("3ra");
        libro.setIdioma("Español");
        libro.setFechaPublicacion(new Date());
        libro.setDescripcion("Un clásico de la literatura.");
        libro.setTipoPasta("Dura");
        libro.setIsbn("978-84-376-0494-7");
        libro.setNumEjemplares(5);
        libro.setPortada("portada.jpg");
        libro.setPresentacion("Tapa dura");
        libro.setPrecio(15.99);
        libro.setAutor(autor);
        libro.setCategoria(categoria);

        Libro guardado = libroRepository.save(libro);
        assertNotNull(guardado);
        System.out.println("✅ Libro guardado: " + guardado);
    }

    @Test
    public void update() {
        Optional<Libro> libroOpt = libroRepository.findById(1);
        assertTrue(libroOpt.isPresent(), "Libro con ID 1 debe existir para actualizar.");

        Libro libro = libroOpt.get();
        libro.setPrecio(20.0);
        libro.setNumEjemplares(10);

        Libro actualizado = libroRepository.save(libro);
        assertEquals(20.0, actualizado.getPrecio());
        System.out.println("✏️ Libro actualizado: " + actualizado);
    }

    @Test
    public void delete() {
        Optional<Libro> libroOpt = libroRepository.findById(1);
        assertTrue(libroOpt.isPresent(), "Libro con ID 1 debe existir para eliminar.");

        libroRepository.deleteById(1);
        assertFalse(libroRepository.findById(1).isPresent());
        System.out.println("🗑 Libro con ID 1 eliminado.");
    }
}
