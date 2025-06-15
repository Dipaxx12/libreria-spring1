package com.distribuida.service;

import com.distribuida.dao.FacturaDetalleRepository;
import com.distribuida.model.FacturaDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaDetalleServiceImpl implements FacturaDetalleService {

    @Autowired
    private FacturaDetalleRepository facturaDetalleRepository;

    @Override
    public List<FacturaDetalle> findAll() {
        return facturaDetalleRepository.findAll();
    }

    @Override
    public FacturaDetalle findOne(int id) {
        Optional<FacturaDetalle> facturaDetalle = facturaDetalleRepository.findById(id);
        return facturaDetalle.orElse(null);
    }

    @Override
    public FacturaDetalle save(FacturaDetalle facturaDetalle) {
        return facturaDetalleRepository.save(facturaDetalle);
    }

    @Override
    public FacturaDetalle update(int id, FacturaDetalle facturaDetalle) {
        FacturaDetalle existente = findOne(id);
        if (existente == null) return null;

        existente.setCantidad(facturaDetalle.getCantidad());
        existente.setSubTotal(facturaDetalle.getSubTotal());
        existente.setFactura(facturaDetalle.getFactura());
        existente.setLibro(facturaDetalle.getLibro());

        return facturaDetalleRepository.save(existente);
    }

    @Override
    public void delete(int id) {
        if (facturaDetalleRepository.existsById(id)) {
            facturaDetalleRepository.deleteById(id);
        }
    }
}
