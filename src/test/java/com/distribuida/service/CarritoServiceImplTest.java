package com.distribuida.service;

import com.distribuida.dao.CarritoItemRepository;
import com.distribuida.dao.CarritoRepository;
import com.distribuida.dao.ClienteRepository;
import com.distribuida.dao.LibroRepository;
import com.distribuida.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarritoServiceImplTest {

    CarritoRepository carritoRepository = mock(CarritoRepository.class);
    CarritoItemRepository carritoItemRepository = mock(CarritoItemRepository.class);
    ClienteRepository clienteRepository = mock(ClienteRepository.class);
    LibroRepository libroRepository = mock(LibroRepository.class);

    CarritoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CarritoServiceImpl(
                carritoRepository, carritoItemRepository, clienteRepository, libroRepository);
    }

    @Test
    void addItem_cuandoItemYaExiste_incrementaCantidadYRecalcula() {
        int clienteId = 1;
        int libroId = 10;

        var cliente = new Cliente();
        cliente.setIdCliente((clienteId));

        var libro = new Libro();
        libro.setIdLibro(libroId);
        libro.setPrecio(20.0);

        var itemExistente = new CarritoItem();
        itemExistente.setIdCarritoItem(100L);
        itemExistente.setLibro(libro);
        itemExistente.setCantidad(1);
        itemExistente.setPrecioUnitario(BigDecimal.valueOf(20.0));
        itemExistente.setTotal(BigDecimal.valueOf(20.0));

        var carrito = new Carrito();
        carrito.setCliente(cliente);
        carrito.setItems(new ArrayList<>(List.of(itemExistente)));

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(carritoRepository.findByCliente_IdCliente((long) clienteId))
                .thenReturn(List.of(carrito));
        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libro));
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(i -> i.getArgument(0));

        var resultado = service.addItem(clienteId, libroId, 2);

        assertNotNull(resultado);
        assertEquals(1, resultado.getItems().size());
        var captor = ArgumentCaptor.forClass(CarritoItem.class);
        verify(carritoItemRepository, atLeastOnce()).save(captor.capture());
        var guardado = captor.getValue();
        assertEquals(3, guardado.getCantidad()); // 1 + 2
        assertEquals(BigDecimal.valueOf(20.0), guardado.getPrecioUnitario());
        assertEquals(BigDecimal.valueOf(60.0), guardado.getTotal()); // 3 * 20
        verify(carritoRepository).save(resultado);
    }
}
