package com.distribuida.service;

import com.distribuida.dao.CarritoRepository;
import com.distribuida.dao.FacturaDetalleRepository;
import com.distribuida.dao.FacturaRepository;
import com.distribuida.dao.LibroRepository;
import com.distribuida.model.Factura;
import com.distribuida.model.FacturaDetalle;
import com.distribuida.model.Libro;
import com.distribuida.service.util.CheckoutMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GuestCheckoutServiceImpl implements GuestCheckoutService {

    private final CarritoRepository carritoRepository;
    private final FacturaRepository facturaRepository;
    private final FacturaDetalleRepository facturaDetalleRepository;
    private final LibroRepository libroRepository;

    private static final double IVA = 0.15d;

    public GuestCheckoutServiceImpl(CarritoRepository carritoRepository,
            FacturaRepository facturaRepository,
            FacturaDetalleRepository facturaDetalleRepository,
            LibroRepository libroRepository) {
        this.carritoRepository = carritoRepository;
        this.facturaRepository = facturaRepository;
        this.facturaDetalleRepository = facturaDetalleRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public Factura checkoutByToken(String token) {
        // 1) Carrito por token
        var carrito = carritoRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("No existe carrito para el token"));

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        // 2) Validar stock por cada item
        for (var item : carrito.getItems()) {
            Libro libro = item.getLibro();
            if (libro.getNumEjemplares() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para: " + libro.getTitulo());
            }
        }

        // 3) Descontar stock y persistir libros
        for (var item : carrito.getItems()) {
            var libro = item.getLibro();
            libro.setNumEjemplares(libro.getNumEjemplares() - item.getCantidad());
            libroRepository.save(libro);
        }

        // 4) Crear y guardar Factura
        String numFactura = "F-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                .format(LocalDateTime.now());
        var factura = CheckoutMapper.construirFacturaDesdeCarrito(carrito, numFactura, IVA);
        factura = facturaRepository.save(factura);

        // 5) Crear y guardar Detalles
        for (var item : carrito.getItems()) {
            FacturaDetalle det = CheckoutMapper.construirDetalle(factura, item);
            facturaDetalleRepository.save(det);
        }

        // 6) Limpiar carrito y persistir
        carrito.getItems().clear();
        carrito.recomputarTotales(BigDecimal.valueOf(IVA));
        carritoRepository.save(carrito);

        // 7) Devolver factura
        return factura;
    }
}
