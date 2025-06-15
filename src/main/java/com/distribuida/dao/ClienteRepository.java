package com.distribuida.dao;

import com.distribuida.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository // es un bean para gestionar persistencia con jpa
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Cliente findByCedula(String cedula);
}
