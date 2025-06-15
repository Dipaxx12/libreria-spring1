package com.distribuida.service;

import com.distribuida.dao.ClienteRepository;
import com.distribuida.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Es un bean de lógica de negocio
public class ClienteServiceImpl implements ClienteService {

    @Autowired  // Anotación para inyección de dependencias
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll(); // ✅ Aquí sí devuelve los datos reales
    }

    @Override
    public Cliente findOne(int id) {

        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElse(null) ;
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente update(int id, Cliente cliente) {
        Cliente clienteExistente = findOne(id);

        if (clienteExistente == null) {
            return null;
        }

        clienteExistente.setCedula(cliente.getCedula());
        clienteExistente.setNombre(cliente.getNombre());
        clienteExistente.setApellido(cliente.getApellido());
        clienteExistente.setTelefono(cliente.getDireccion());
        clienteExistente.setCorreo(cliente.getTelefono());

        return clienteRepository.save(clienteExistente);
    }


    @Override
    public void delete(int id) {
        if(clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        }
    }
}
