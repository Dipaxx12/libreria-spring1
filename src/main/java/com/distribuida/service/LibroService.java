package com.distribuida.service;

import com.distribuida.model.Libro;

import java.util.List;

public interface LibroService {
    List<Libro> findAll();
    Libro findOne(int id);
    Libro save(Libro libro);
    Libro update(int id, Libro libro);
    void delete(int id);
}
