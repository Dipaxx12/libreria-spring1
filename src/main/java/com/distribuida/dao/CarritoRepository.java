package com.distribuida.dao;

import com.distribuida.model.Carrito;
import com.distribuida.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByToken(String token);

    List<Carrito> findByCliente_IdCliente(Long idCliente);

    // 👇 Añadimos este, para usarlo en getByClienteId como tu profe
    Optional<Carrito> findByCliente(Cliente cliente);
}
