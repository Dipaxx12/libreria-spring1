package com.distribuida.service;

import com.distribuida.dao.LibroRepository;
import com.distribuida.model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Override
    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    @Override
    public Libro findOne(int id) {
        Optional<Libro> libro = libroRepository.findById(id);
        return libro.orElse(null);
    }

    @Override
    public Libro save(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public Libro update(int id, Libro libro) {
        Libro existente = findOne(id);
        if (existente == null) return null;

        existente.setTitulo(libro.getTitulo());
        existente.setEditorial(libro.getEditorial());
        existente.setNumPaginas(libro.getNumPaginas());
        existente.setEdicion(libro.getEdicion());
        existente.setIdioma(libro.getIdioma());
        existente.setFechaPublicacion(libro.getFechaPublicacion());
        existente.setDescripcion(libro.getDescripcion());
        existente.setTipoPasta(libro.getTipoPasta());
        existente.setIsbn(libro.getIsbn());
        existente.setNumEjemplares(libro.getNumEjemplares());
        existente.setPortada(libro.getPortada());
        existente.setPresentacion(libro.getPresentacion());
        existente.setPrecio(libro.getPrecio());
        existente.setCategoria(libro.getCategoria());
        existente.setAutor(libro.getAutor());

        return libroRepository.save(existente);
    }

    @Override
    public void delete(int id) {
        if (libroRepository.existsById(id)) {
            libroRepository.deleteById(id);
        }
    }
}
