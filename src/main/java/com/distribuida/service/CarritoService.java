package com.distribuida.service;

import com.distribuida.model.Carrito;

public interface CarritoService {

    // --- Operando por clienteId ---
    Carrito getOrCreateByClienteId(int clienteId, String token);

    Carrito addItem(int clienteId, int libroId, int cantidad);

    Carrito updateItemCantidad(int clienteId, long carritoItemId, int nuevaCantidad);

    void removeItem(int clienteId, long carritoItemId);

    void clear(int clienteId);

    Carrito getClienteById(int clienteId);

    // --- Operando por token (guest/sesión) ---
    Carrito getOrCreateByToken(String token);

    Carrito addItem(String token, int libroId, int cantidad);

    Carrito updateItemCantidad(String token, long carritoItemId, int nuevaCantidad);

    void removeItem(String token, long carritoItemId);

    void clearByToken(String token);

    Carrito getByToken(String token);
}
