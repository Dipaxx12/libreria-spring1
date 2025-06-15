package com.distribuida.dao;

import com.distribuida.model.Cliente;
import com.distribuida.model.Factura;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback(false)
public class FacturaRepositoryTestIntegration {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;
    private Factura factura;

    @BeforeEach
    void setup() {
        // Crear cliente
        cliente = new Cliente();
        cliente.setNombre("Carlos");
        cliente.setApellido("Mena");
        cliente.setCedula("1234567890");
        cliente.setCorreo("carlos@correo.com");
        cliente.setDireccion("Av. Ecuador");
        cliente.setTelefono("099888777");
        cliente = clienteRepository.save(cliente);

        // Crear factura
        factura = new Factura();
        factura.setNumFactura("F001-XYZ");
        factura.setFecha(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        factura.setTotalNeto(100.0);
        factura.setIva(12.0);
        factura.setTotal(112.0);
        factura.setCliente(cliente);
        factura = facturaRepository.save(factura);
    }

    @Test
    public void findAll() {
        List<Factura> facturas = facturaRepository.findAll();
        assertNotNull(facturas);
        assertTrue(facturas.size() > 0);
        facturas.forEach(System.out::println);
    }

    @Test
    public void findOne() {
        var encontrada = facturaRepository.findById(factura.getIdFactura());
        assertTrue(encontrada.isPresent(), "La factura debe existir.");
        System.out.println(encontrada.get());
    }

    @Test
    public void save() {
        Factura nueva = new Factura();
        nueva.setNumFactura("F002-ABC");
        nueva.setFecha(new Date());
        nueva.setTotalNeto(80.0);
        nueva.setIva(9.6);
        nueva.setTotal(89.6);
        nueva.setCliente(cliente);

        Factura guardada = facturaRepository.save(nueva);
        assertNotNull(guardada);
        assertTrue(guardada.getIdFactura() > 0);
        System.out.println("✅ Factura guardada: " + guardada);
    }

    @Test
    public void update() {
        factura.setTotalNeto(200.0);
        factura.setIva(24.0);
        factura.setTotal(224.0);

        Factura actualizada = facturaRepository.save(factura);
        assertEquals(200.0, actualizada.getTotalNeto());
        System.out.println("✏️ Factura actualizada: " + actualizada);
    }

    @Test
    public void delete() {
        int id = factura.getIdFactura();
        facturaRepository.deleteById(id);
        assertFalse(facturaRepository.findById(id).isPresent());
        System.out.println("🗑 Factura con ID " + id + " eliminada correctamente.");
    }
}
