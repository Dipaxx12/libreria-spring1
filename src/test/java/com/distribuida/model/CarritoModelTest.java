package com.distribuida.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CarritoModelTest {

    @Test
    void testRecomputarTotales() {
        Carrito carrito = new Carrito();

        // Crear items
        CarritoItem item1 = new CarritoItem();
        item1.setCantidad(2);
        item1.setPrecioUnitario(new BigDecimal("10.00"));
        item1.setTotal(new BigDecimal("20.00"));

        CarritoItem item2 = new CarritoItem();
        item2.setCantidad(1);
        item2.setPrecioUnitario(new BigDecimal("15.00"));
        item2.setTotal(new BigDecimal("15.00"));

        List<CarritoItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        carrito.setItems(items);

        // Recalcular totales con IVA 12%
        carrito.recomputarTotales(new BigDecimal("0.12"));

        assertThat(carrito.getSubtotal()).isEqualByComparingTo("35.00");
        assertThat(carrito.getImpuestos()).isEqualByComparingTo("4.20");
        assertThat(carrito.getTotal()).isEqualByComparingTo("39.20");
    }
}
