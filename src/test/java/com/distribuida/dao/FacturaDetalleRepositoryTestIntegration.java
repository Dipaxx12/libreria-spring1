package com.distribuida.dao;

import com.distribuida.model.Factura;
import com.distribuida.model.FacturaDetalle;
import com.distribuida.model.Libro;
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
public class FacturaDetalleRepositoryTestIntegration {

    @Autowired
    private FacturaDetalleRepository facturaDetalleRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Test
    public void findAll() {
        List<FacturaDetalle> detalles = facturaDetalleRepository.findAll();
        assertNotNull(detalles);
        assertTrue(detalles.size() > 0);
        detalles.forEach(System.out::println);
    }

    @Test
    public void findOne() {
        Optional<FacturaDetalle> detalle = facturaDetalleRepository.findById(3);
        assertTrue(detalle.isPresent(), "El detalle con ID 1 debe existir.");
        System.out.println(detalle.get());
    }

    @Test
    public void save() {
        Optional<Factura> facturaOpt = facturaRepository.findById(1);
        Optional<Libro> libroOpt = libroRepository.findById(1);

        assertTrue(facturaOpt.isPresent(), "Debe existir factura con ID 1.");
        assertTrue(libroOpt.isPresent(), "Debe existir libro con ID 1.");

        FacturaDetalle detalle = new FacturaDetalle();
        detalle.setCantidad(3);
        detalle.setSubTotal(45.0);
        detalle.setFactura(facturaOpt.get());
        detalle.setLibro(libroOpt.get());

        FacturaDetalle guardado = facturaDetalleRepository.save(detalle);
        assertNotNull(guardado);
        System.out.println("✅ Detalle guardado: " + guardado);
    }

    @Test
    public void update() {
        Optional<FacturaDetalle> detalleOpt = facturaDetalleRepository.findById(1);
        assertTrue(detalleOpt.isPresent(), "El detalle con ID 1 debe existir para actualizar.");

        FacturaDetalle detalle = detalleOpt.get();
        detalle.setCantidad(5);
        detalle.setSubTotal(75.0);

        FacturaDetalle actualizado = facturaDetalleRepository.save(detalle);
        assertEquals(5, actualizado.getCantidad());
        System.out.println("✏️ Detalle actualizado: " + actualizado);
    }

    @Test
    public void delete() {
        Optional<FacturaDetalle> detalleOpt = facturaDetalleRepository.findById(1);
        assertTrue(detalleOpt.isPresent(), "El detalle con ID 1 debe existir para eliminar.");

        facturaDetalleRepository.deleteById(1);
        assertFalse(facturaDetalleRepository.findById(1).isPresent());
        System.out.println("🗑 Detalle con ID 1 eliminado correctamente.");
    }
}
