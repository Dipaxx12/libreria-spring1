package com.distribuida.model;

import jakarta.persistence.*;

@Entity
@Table(name = "factura_detalle")
public class FacturaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura_detalle")
    private int idFacturaDetalle;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "sub_total")
    private Double subTotal;

    @ManyToOne
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura")
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "id_libro", referencedColumnName = "id_libro")
    private Libro libro;

    public FacturaDetalle() {}

    public FacturaDetalle(int idFacturaDetalle, int cantidad, Double subTotal, Factura factura, Libro libro) {
        this.idFacturaDetalle = idFacturaDetalle;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
        this.factura = factura;
        this.libro = libro;
    }

    public int getIdFacturaDetalle() {
        return idFacturaDetalle;
    }

    public void setIdFacturaDetalle(int idFacturaDetalle) {
        this.idFacturaDetalle = idFacturaDetalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Override
    public String toString() {
        return "FacturaDetalle{" +
                "idFacturaDetalle=" + idFacturaDetalle +
                ", cantidad=" + cantidad +
                ", subTotal=" + subTotal +
                ", factura=" + (factura != null ? factura.getIdFactura() : "null") +
                ", libro=" + (libro != null ? libro.getIdLibro() : "null") +
                '}';
    }
}
