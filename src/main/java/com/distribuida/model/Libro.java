package com.distribuida.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private int idLibro;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "editorial")
    private String editorial;

    @Column(name = "num_paginas")
    private int numPaginas;

    @Column(name = "edicion")
    private String edicion;

    @Column(name = "idioma")
    private String idioma;

    @Column(name = "fecha_publicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaPublicacion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo_pasta")
    private String tipoPasta;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "num_ejemplares")
    private int numEjemplares;

    @Column(name = "portada")
    private String portada;

    @Column(name = "presentacion")
    private String presentacion;

    @Column(name = "precio")
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_autor", referencedColumnName = "id_autor")
    private Autor autor;

    public Libro() {}

    public Libro(int idLibro, String titulo, String editorial, int numPaginas, String edicion, String idioma,
                 Date fechaPublicacion, String descripcion, String tipoPasta, String isbn, int numEjemplares,
                 String portada, String presentacion, Double precio, Categoria categoria, Autor autor) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.editorial = editorial;
        this.numPaginas = numPaginas;
        this.edicion = edicion;
        this.idioma = idioma;
        this.fechaPublicacion = fechaPublicacion;
        this.descripcion = descripcion;
        this.tipoPasta = tipoPasta;
        this.isbn = isbn;
        this.numEjemplares = numEjemplares;
        this.portada = portada;
        this.presentacion = presentacion;
        this.precio = precio;
        this.categoria = categoria;
        this.autor = autor;
    }

    // Getters y Setters...

    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }

    public String getEdicion() { return edicion; }
    public void setEdicion(String edicion) { this.edicion = edicion; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public Date getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Date fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipoPasta() { return tipoPasta; }
    public void setTipoPasta(String tipoPasta) { this.tipoPasta = tipoPasta; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getNumEjemplares() { return numEjemplares; }
    public void setNumEjemplares(int numEjemplares) { this.numEjemplares = numEjemplares; }

    public String getPortada() { return portada; }
    public void setPortada(String portada) { this.portada = portada; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    @Override
    public String toString() {
        return "Libro{" +
                "idLibro=" + idLibro +
                ", titulo='" + titulo + '\'' +
                ", editorial='" + editorial + '\'' +
                ", numPaginas=" + numPaginas +
                ", edicion='" + edicion + '\'' +
                ", idioma='" + idioma + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", descripcion='" + descripcion + '\'' +
                ", tipoPasta='" + tipoPasta + '\'' +
                ", isbn='" + isbn + '\'' +
                ", numEjemplares=" + numEjemplares +
                ", portada='" + portada + '\'' +
                ", presentacion='" + presentacion + '\'' +
                ", precio=" + precio +
                ", categoria=" + (categoria != null ? categoria.getIdCategoria() : "null") +
                ", autor=" + (autor != null ? autor.getIdAutor() : "null") +
                '}';
    }
}
