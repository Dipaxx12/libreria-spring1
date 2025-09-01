package com.distribuida.service;

import com.distribuida.dao.*;
import com.distribuida.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuestCheckoutServiceImplTest {

    CarritoRepository carritoRepository = mock(CarritoRepository.class);
    FacturaRepository facturaRepository = mock(FacturaRepository.class);
    FacturaDetalleRepository facturaDetalleRepository = mock(FacturaDetalleRepository.class);
    LibroRepository libroRepository = mock(LibroRepository.class);

    @Test
    void checkoutByToken_lanzaExcepcion_siStockInsuficiente() {
        var service = new GuestCheckoutServiceImpl(
                carritoRepository, facturaRepository, facturaDetalleRepository, libroRepository);

        var libro = new Libro();
        libro.setIdLibro(10);
        libro.setTitulo("DDD");
        libro.setNumEjemplares(3); // stock

        var item = new CarritoItem();
        item.setLibro(libro);
        item.setCantidad(5); // solicita más que stock

        var carrito = new Carrito();
        carrito.setItems(List.of(item));

        when(carritoRepository.findByToken("TKN")).thenReturn(Optional.of(carrito));

        var ex = assertThrows(IllegalStateException.class,
                () -> service.checkoutByToken("TKN"));
        assertTrue(ex.getMessage().toLowerCase().contains("stock insuficiente"));
        verifyNoInteractions(facturaRepository, facturaDetalleRepository);
    }
}
