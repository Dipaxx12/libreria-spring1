package com.distribuida.dao;

import com.distribuida.model.Cliente;
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
public class ClienteRepositoryTestIntegracion {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void findAll() {
        List<Cliente> clientes = clienteRepository.findAll();

        // Asserts
        assertNotNull(clientes, "La lista de clientes no debe ser null");
        assertFalse(clientes.isEmpty(), "La lista de clientes no debe estar vacía");

        for (Cliente item : clientes) {
            System.out.println(item.toString());
        }
    }

    @Test
    public void findOne() {
        Optional<Cliente> clienteOpt = clienteRepository.findById(2);

        // Asserts
        assertTrue(clienteOpt.isPresent(), "El cliente con ID 2 debe existir");

        Cliente cliente = clienteOpt.get();
        assertNotNull(cliente.getNombre(), "El nombre no debe ser null");
        assertNotNull(cliente.getCedula(), "La cédula no debe ser null");

        System.out.println("✅ Cliente encontrado: " + cliente.toString());
    }

    @Test
    public void save() {
        Cliente cliente = new Cliente(
                0,
                "1701234567",
                "Juan",
                "Taipe",
                "Av. por ahi",
                "0987654321",
                "juan.taipe@correo.com"
        );

        Cliente clienteGuardado = clienteRepository.save(cliente);

        // Asserts
        assertNotNull(clienteGuardado, "El cliente guardado no debe ser null");
        assertNotEquals(0, clienteGuardado.getIdCliente(), "El ID debe ser generado");
        assertEquals("Juan", clienteGuardado.getNombre());
        assertEquals("Taipe", clienteGuardado.getApellido());
    }

    @Test
    public void update() {
        Optional<Cliente> clienteOpt = clienteRepository.findById(2);
        assertTrue(clienteOpt.isPresent(), "Debe existir un cliente con ID 2 para actualizar");

        Cliente cliente = clienteOpt.get();
        cliente.setNombre("Juanito");
        cliente.setApellido("Taipe Actualizado");
        cliente.setDireccion("Nueva dirección");
        cliente.setTelefono("0999999999");
        cliente.setCorreo("juanito.actualizado@correo.com");

        Cliente actualizado = clienteRepository.save(cliente);

        // Asserts
        assertEquals("Juanito", actualizado.getNombre());
        assertEquals("Taipe Actualizado", actualizado.getApellido());
        assertEquals("0999999999", actualizado.getTelefono());

        System.out.println("✅ Cliente actualizado: " + actualizado.toString());
    }

    @Test
    public void delete() {
        int idAEliminar = 2;

        boolean existeAntes = clienteRepository.existsById(idAEliminar);
        if (!existeAntes) {
            fail("❌ No se puede eliminar. Cliente con ID " + idAEliminar + " no existe.");
        }

        clienteRepository.deleteById(idAEliminar);

        boolean existeDespues = clienteRepository.existsById(idAEliminar);
        assertFalse(existeDespues, "El cliente debe haber sido eliminado");

        System.out.println("🗑 Cliente con ID " + idAEliminar + " eliminado correctamente.");
    }
}
