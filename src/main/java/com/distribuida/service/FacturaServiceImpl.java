package com.distribuida.service;

import com.distribuida.dao.FacturaRepository;
import com.distribuida.model.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    @Override
    public Factura findOne(int id) {
        Optional<Factura> factura = facturaRepository.findById(id);
        return factura.orElse(null);
    }

    @Override
    public Factura save(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    public Factura update(int id, Factura factura) {
        Factura existente = findOne(id);
        if (existente == null) return null;

        existente.setNumFactura(factura.getNumFactura());
        existente.setFecha(factura.getFecha());
        existente.setTotalNeto(factura.getTotalNeto());
        existente.setIva(factura.getIva());
        existente.setTotal(factura.getTotal());
        existente.setCliente(factura.getCliente());

        return facturaRepository.save(existente);
    }

    @Override
    public void delete(int id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
        }
    }
}
