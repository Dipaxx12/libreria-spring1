package com.distribuida.dao;

import com.distribuida.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // Puedes agregar métodos personalizados si lo necesitas, por ejemplo:
    Categoria findByCategoria(String categoria);
}
