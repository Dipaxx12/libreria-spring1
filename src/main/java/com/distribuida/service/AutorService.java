package com.distribuida.service;

import com.distribuida.model.Autor;

import java.util.List;

public interface AutorService {

    List<Autor> findAll();

    Autor findOne(int id);

    Autor save(Autor autor);

    Autor update(int id, Autor autor);

    void delete(int id);
}
