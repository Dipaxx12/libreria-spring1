package com.distribuida.dao;

import com.distribuida.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {

    // Puedes agregar métodos personalizados si lo necesitas
    Autor findByNombre(String nombre);
}
