package com.distribuida.dao;

import com.distribuida.model.Categoria;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback(false)
public class CategoriaRepositoryTestIntegration {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    public void findAll() {
        List<Categoria> categorias = categoriaRepository.findAll();
        assertNotNull(categorias);
        assertTrue(categorias.size() > 0);
        for (Categoria item : categorias) {
            System.out.println(item.toString());
        }
    }

    @Test
    public void findOne() {
        Optional<Categoria> categoria = categoriaRepository.findById(1);
        assertTrue(categoria.isPresent(), "La categoría con ID 1 debe existir.");
        System.out.println(categoria.get().toString());
    }

    @Test
    public void save() {
        Categoria categoria = new Categoria(
                0,
                "Tecnología",
                "Libros de informática y programación"
        );
        Categoria guardado = categoriaRepository.save(categoria);
        assertNotNull(guardado);
        assertEquals("Tecnología", guardado.getCategoria());
        assertEquals("Libros de informática y programación", guardado.getDescripcion());
        System.out.println("✅ Categoría guardada: " + guardado);
    }

    @Test
    public void update() {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(1);
        assertTrue(categoriaOpt.isPresent(), "La categoría con ID 1 debe existir para actualizar.");
        Categoria categoria = categoriaOpt.get();

        categoria.setCategoria("Tecnología Avanzada");
        categoria.setDescripcion("Categoría actualizada de informática");

        Categoria actualizada = categoriaRepository.save(categoria);
        assertEquals("Tecnología Avanzada", actualizada.getCategoria());
        assertEquals("Categoría actualizada de informática", actualizada.getDescripcion());
        System.out.println("✅ Categoría actualizada: " + actualizada);
    }

    @Test
    public void delete() {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(2);
        assertTrue(categoriaOpt.isPresent(), "La categoría con ID 2 debe existir para eliminar.");
        categoriaRepository.deleteById(2);
        System.out.println("🗑 Categoría con ID 2 eliminada correctamente.");
    }
}
