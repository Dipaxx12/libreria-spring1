package com.distribuida.service;

import com.distribuida.model.Factura;

import java.util.List;

public interface FacturaService {
    List<Factura> findAll();
    Factura findOne(int id);
    Factura save(Factura factura);
    Factura update(int id, Factura factura);
    void delete(int id);
}
