package com.distribuida.service;

import com.distribuida.dao.CarritoItemRepository;
import com.distribuida.dao.CarritoRepository;
import com.distribuida.dao.ClienteRepository;
import com.distribuida.dao.LibroRepository;
import com.distribuida.model.Carrito;
import com.distribuida.model.CarritoItem;
import com.distribuida.model.Cliente;
import com.distribuida.model.Libro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ClienteRepository clienteRepository;
    private final LibroRepository libroRepository;

    private static final BigDecimal IVA = new BigDecimal("0.15");

    public CarritoServiceImpl(CarritoRepository carritoRepository,
            CarritoItemRepository carritoItemRepository,
            ClienteRepository clienteRepository,
            LibroRepository libroRepository) {
        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.clienteRepository = clienteRepository;
        this.libroRepository = libroRepository;
    }

    // =========================================================
    // Operaciones por clienteId
    // =========================================================
    @Override
    @Transactional
    public Carrito getOrCreateByClienteId(int clienteId, String token) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado " + clienteId));

        var carritoOpt = carritoRepository.findByCliente_IdCliente((long) clienteId)
                .stream().findFirst();

        if (carritoOpt.isPresent())
            return carritoOpt.get();

        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        carrito.setToken(token);
        carrito.recomputarTotales(IVA);
        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public Carrito addItem(int clienteId, int libroId, int cantidad) {
        if (cantidad <= 0)
            throw new IllegalArgumentException("Cantidad debe ser > 0");

        Carrito carrito = getOrCreateByClienteId(clienteId, null);
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado " + libroId));

        // ¿Ya existe el ítem con ese libro?
        CarritoItem item = carrito.getItems().stream()
                .filter(ci -> ci.getLibro() != null && ci.getLibro().getIdLibro() == libroId)
                .findFirst()
                .orElse(null);

        if (item != null) {
            item.setCantidad(item.getCantidad() + cantidad);
            item.setPrecioUnitario(BigDecimal.valueOf(libro.getPrecio()));
            item.setTotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())));
            carritoItemRepository.save(item);
        } else {
            item = new CarritoItem();
            item.setCarrito(carrito);
            item.setLibro(libro);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(BigDecimal.valueOf(libro.getPrecio()));
            item.setTotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));
            carrito.getItems().add(item);
        }

        carrito.recomputarTotales(IVA);
        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public Carrito updateItemCantidad(int clienteId, long carritoItemId, int nuevaCantidad) {
        if (nuevaCantidad < 0)
            throw new IllegalArgumentException("nuevaCantidad no puede ser negativa");

        Carrito carrito = getOrCreateByClienteId(clienteId, null);

        CarritoItem item = carrito.getItems().stream()
                .filter(ci -> ci.getIdCarritoItem() != null && ci.getIdCarritoItem() == carritoItemId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado " + carritoItemId));

        if (nuevaCantidad == 0) {
            carrito.getItems().remove(item);
            carritoItemRepository.delete(item);
        } else {
            item.setCantidad(nuevaCantidad);
            if (item.getLibro() != null) {
                item.setPrecioUnitario(BigDecimal.valueOf(item.getLibro().getPrecio()));
            }
            item.setTotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(nuevaCantidad)));
            carritoItemRepository.save(item);
        }

        carrito.recomputarTotales(IVA);
        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void removeItem(int clienteId, long carritoItemId) {
        Carrito carrito = getOrCreateByClienteId(clienteId, null);

        carrito.getItems().removeIf(ci -> {
            boolean match = ci.getIdCarritoItem() != null && ci.getIdCarritoItem() == carritoItemId;
            if (match)
                carritoItemRepository.delete(ci);
            return match;
        });

        carrito.recomputarTotales(IVA);
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void clear(int clienteId) {
        Carrito carrito = getOrCreateByClienteId(clienteId, null);
        carrito.getItems().clear();
        carrito.recomputarTotales(IVA);
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional(readOnly = true)
    public Carrito getClienteById(int clienteId) {
        var cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));

        return carritoRepository.findByCliente(cliente)
                .orElseGet(() -> {
                    var c = new Carrito();
                    c.setCliente(cliente);
                    return c;
                });
    }

    // =========================================================
    // Operaciones por token
    // =========================================================
    @Override
    @Transactional
    public Carrito getOrCreateByToken(String token) {
        return carritoRepository.findByToken(token).orElseGet(() -> {
            Carrito c = new Carrito();
            c.setToken(token);
            c.recomputarTotales(IVA);
            return carritoRepository.save(c);
        });
    }

    @Override
    @Transactional
    public Carrito addItem(String token, int libroId, int cantidad) {
        if (cantidad <= 0)
            throw new IllegalArgumentException("Cantidad debe ser > 0");

        Carrito carrito = getOrCreateByToken(token);
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado " + libroId));

        CarritoItem item = carrito.getItems().stream()
                .filter(ci -> ci.getLibro() != null && ci.getLibro().getIdLibro() == libroId)
                .findFirst().orElse(null);

        if (item != null) {
            item.setCantidad(item.getCantidad() + cantidad);
            item.setPrecioUnitario(BigDecimal.valueOf(libro.getPrecio()));
            item.setTotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())));
            carritoItemRepository.save(item);
        } else {
            item = new CarritoItem();
            item.setCarrito(carrito);
            item.setLibro(libro);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(BigDecimal.valueOf(libro.getPrecio()));
            item.setTotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));
            carrito.getItems().add(item);
        }

        carrito.recomputarTotales(IVA);
        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public Carrito updateItemCantidad(String token, long carritoItemId, int nuevaCantidad) {
        if (nuevaCantidad < 0)
            throw new IllegalArgumentException("nuevaCantidad no puede ser negativa");

        Carrito carrito = getOrCreateByToken(token);

        CarritoItem item = carrito.getItems().stream()
                .filter(ci -> ci.getIdCarritoItem() != null && ci.getIdCarritoItem() == carritoItemId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado " + carritoItemId));

        if (nuevaCantidad == 0) {
            carrito.getItems().remove(item);
            carritoItemRepository.delete(item);
        } else {
            item.setCantidad(nuevaCantidad);
            if (item.getLibro() != null) {
                item.setPrecioUnitario(BigDecimal.valueOf(item.getLibro().getPrecio()));
            }
            item.setTotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(nuevaCantidad)));
            carritoItemRepository.save(item);
        }

        carrito.recomputarTotales(IVA);
        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void removeItem(String token, long carritoItemId) {
        Carrito carrito = getOrCreateByToken(token);

        carrito.getItems().removeIf(ci -> {
            boolean match = ci.getIdCarritoItem() != null && ci.getIdCarritoItem() == carritoItemId;
            if (match)
                carritoItemRepository.delete(ci);
            return match;
        });

        carrito.recomputarTotales(IVA);
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void clearByToken(String token) {
        Carrito carrito = getOrCreateByToken(token);
        carrito.getItems().clear();
        carrito.recomputarTotales(IVA);
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional(readOnly = true)
    public Carrito getByToken(String token) {
        return carritoRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado para token " + token));
    }
}
