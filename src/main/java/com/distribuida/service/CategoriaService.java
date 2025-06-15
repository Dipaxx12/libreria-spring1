package com.distribuida.service;

import com.distribuida.model.Categoria;

import java.util.List;

public interface CategoriaService {

    List<Categoria> findAll();

    Categoria findOne(int id);

    Categoria save(Categoria categoria);

    Categoria update(int id, Categoria categoria);

    void delete(int id);
}
