package com.distribuida.dao;

import com.distribuida.model.Carrito;
import com.distribuida.model.Cliente;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback(false)
public class CarritoRepositoryTestIntegracion {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void save() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setDireccion("Quito");
        cliente.setCorreo("juan@test.com");
        clienteRepository.save(cliente);

        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        carrito.setToken("token-123");
        carrito.setSubtotal(BigDecimal.ZERO);
        carrito.setTotal(BigDecimal.ZERO);

        Carrito guardado = carritoRepository.save(carrito);

        assertNotNull(guardado, "El carrito guardado no debe ser null");
        assertNotNull(guardado.getIdCarrito(), "El carrito debe tener un ID generado");
        assertEquals("token-123", guardado.getToken());
    }

    @Test
    public void findOne() {
        Optional<Carrito> opt = carritoRepository.findById(1L);
        assertTrue(opt.isPresent(), "El carrito con ID 1 debería existir (si ya lo insertaste antes)");
    }
}
