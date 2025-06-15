package com.distribuida.dao;

import com.distribuida.model.Autor;
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
public class AutorRepositoryTestIntegration {

    @Autowired
    private AutorRepository autorRepository;

    @Test
    public void findAll() {
        List<Autor> autores = autorRepository.findAll();
        assertNotNull(autores);
        assertTrue(autores.size() > 0);
        for (Autor item : autores) {
            System.out.println(item.toString());
        }
    }

    @Test
    public void findOne() {
        // Creamos un autor temporal para buscar
        Autor autor = new Autor(0, "TestNombre", "TestApellido", "TestPais", "TestDireccion", "0000000000", "test@correo.com");
        Autor guardado = autorRepository.save(autor);

        Optional<Autor> encontrado = autorRepository.findById(guardado.getIdAutor());
        assertTrue(encontrado.isPresent(), "El autor recién creado debe existir");
        assertEquals("TestNombre", encontrado.get().getNombre());
        System.out.println("🔍 Autor encontrado: " + encontrado.get());
    }

    @Test
    public void save() {
        Autor autor = new Autor(
                0,
                "Nuevo",
                "Autor",
                "Argentina",
                "Av. 123",
                "0990000000",
                "nuevo.autor@correo.com"
        );
        Autor guardado = autorRepository.save(autor);
        assertNotNull(guardado);
        assertEquals("Nuevo", guardado.getNombre());
        assertTrue(guardado.getIdAutor() > 0);
        System.out.println("✅ Autor guardado: " + guardado);
    }

    @Test
    public void update() {
        // Creamos un autor para luego actualizarlo
        Autor autor = new Autor(0, "ViejoNombre", "ViejoApellido", "Ecuador", "Calle Vieja", "0999999999", "viejo@correo.com");
        Autor guardado = autorRepository.save(autor);

        // Actualizamos
        guardado.setNombre("NombreActualizado");
        guardado.setPais("Brasil");
        Autor actualizado = autorRepository.save(guardado);

        assertEquals("NombreActualizado", actualizado.getNombre());
        assertEquals("Brasil", actualizado.getPais());
        System.out.println("🔁 Autor actualizado: " + actualizado);
    }

    @Test
    public void delete() {
        // Creamos un autor temporal
        Autor autor = new Autor(0, "AEliminar", "Temporal", "Chile", "Av. Sur", "1234567890", "eliminar@correo.com");
        Autor guardado = autorRepository.save(autor);

        // Eliminamos
        int id = guardado.getIdAutor();
        autorRepository.deleteById(id);
        Optional<Autor> eliminado = autorRepository.findById(id);

        assertFalse(eliminado.isPresent(), "El autor debería haber sido eliminado");
        System.out.println("🗑 Autor eliminado correctamente con ID: " + id);
    }
}
