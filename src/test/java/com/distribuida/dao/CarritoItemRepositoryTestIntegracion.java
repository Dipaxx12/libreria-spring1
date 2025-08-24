package com.distribuida.dao;

import com.distribuida.model.Carrito;
import com.distribuida.model.CarritoItem;
import com.distribuida.model.Cliente;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback(false)
public class CarritoItemRepositoryTestIntegracion {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Test
    void testGuardarItemEnCarrito() {
        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Maria");
        cliente.setApellido("Lopez");
        cliente.setDireccion("Cumbayá");
        cliente.setCorreo("maria@test.com");
        clienteRepository.save(cliente);

        // Crear carrito
        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        carrito.setToken("token-456");
        carritoRepository.save(carrito);

        // Crear item
        CarritoItem item = new CarritoItem();
        item.setCarrito(carrito);
        item.setNombre("Libro de Java");
        item.setCantidad(2);
        item.setPrecioUnitario(new BigDecimal("25.00"));
        item.setTotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())));

        CarritoItem guardado = carritoItemRepository.save(item);

        // Verificar
        assertThat(guardado.getIdCarritoItem()).isNotNull();
        assertThat(guardado.getCarrito().getIdCarrito()).isEqualTo(carrito.getIdCarrito());
        assertThat(guardado.getNombre()).isEqualTo("Libro de Java");
        assertThat(guardado.getCantidad()).isEqualTo(2);
        assertThat(guardado.getTotal()).isEqualByComparingTo("50.00");

        System.out.println("✅ Item guardado en carrito con ID: " + guardado.getIdCarritoItem());
    }
}
