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

        assertNotNull(clientes, "La lista de clientes no debe ser null");
        assertFalse(clientes.isEmpty(), "La lista de clientes no debe estar vacía");

        clientes.forEach(System.out::println);
    }

    @Test
    public void findOne() {
        Cliente cliente = new Cliente(
                0,
                "1800000001",
                "Ana",
                "Rojas",
                "Calle Falsa 123",
                "0987654321",
                "ana.rojas@example.com");
        Cliente guardado = clienteRepository.save(cliente);

        Optional<Cliente> clienteOpt = clienteRepository.findById(guardado.getIdCliente());

        assertTrue(clienteOpt.isPresent(), "El cliente guardado debe existir");

        Cliente encontrado = clienteOpt.get();
        assertEquals("Ana", encontrado.getNombre());
        assertEquals("Rojas", encontrado.getApellido());

        System.out.println("✅ Cliente encontrado: " + encontrado.toString());
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
                "juan.taipe@correo.com");

        Cliente clienteGuardado = clienteRepository.save(cliente);

        assertNotNull(clienteGuardado, "El cliente guardado no debe ser null");
        assertNotEquals(0, clienteGuardado.getIdCliente(), "El ID debe ser generado");
        assertEquals("Juan", clienteGuardado.getNombre());
        assertEquals("Taipe", clienteGuardado.getApellido());
    }

    @Test
    public void update() {
        Cliente cliente = new Cliente(
                0,
                "1711111111",
                "Carlos",
                "Mendoza",
                "Dirección antigua",
                "0991111111",
                "carlos@correo.com");
        Cliente guardado = clienteRepository.save(cliente);

        guardado.setNombre("Carlos Actualizado");
        guardado.setApellido("Mendoza P.");
        guardado.setDireccion("Nueva dirección");
        guardado.setTelefono("0999999999");
        guardado.setCorreo("carlos.actualizado@correo.com");

        Cliente actualizado = clienteRepository.save(guardado);

        assertEquals("Carlos Actualizado", actualizado.getNombre());
        assertEquals("Mendoza P.", actualizado.getApellido());
        assertEquals("0999999999", actualizado.getTelefono());

        System.out.println("✅ Cliente actualizado: " + actualizado.toString());
    }

    @Test
    public void delete() {
        Cliente cliente = new Cliente(
                0,
                "1899999999",
                "Lucía",
                "Velasco",
                "Calle 10",
                "0966666666",
                "lucia@correo.com");
        Cliente guardado = clienteRepository.save(cliente);
        int idAEliminar = guardado.getIdCliente();

        assertTrue(clienteRepository.existsById(idAEliminar));

        clienteRepository.deleteById(idAEliminar);

        assertFalse(clienteRepository.existsById(idAEliminar), "El cliente debe haber sido eliminado");

        System.out.println("🗑 Cliente con ID " + idAEliminar + " eliminado correctamente.");
    }
}
