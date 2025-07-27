package com.distribuida.controller;

import com.distribuida.model.Cliente;
import com.distribuida.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClienteControllerTestUnitaria {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setIdCliente(1);
        cliente.setCedula("1755564901");
        cliente.setNombre("Pedro");
        cliente.setApellido("Hidalgo");
        cliente.setDireccion("puembo");
        cliente.setTelefono("0979272218");
        cliente.setCorreo("dieg0stax12@gmail.com");
    }

    @Test
    public void testFindAll() {
        when(clienteService.findAll()).thenReturn(List.of(cliente));
        ResponseEntity<List<Cliente>> response = clienteController.findAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(clienteService, times(1)).findAll();
    }

    @Test
    public void testFinOne() {
        when(clienteService.findOne(1)).thenReturn(cliente);
        ResponseEntity<Cliente> response = clienteController.findOne(1);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cliente.getNombre(), response.getBody().getNombre());
    }

    @Test
    public void testFindOneNoExistente() {
        when(clienteService.findOne(2)).thenReturn(null);
        ResponseEntity<Cliente> response = clienteController.findOne(2);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testSave() {
        when(clienteService.save(cliente)).thenReturn(cliente);
        ResponseEntity<Cliente> response = clienteController.save(cliente);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Pedro", response.getBody().getNombre());
    }

    @Test
    public void testUpdate() {
        when(clienteService.update(1, cliente)).thenReturn(cliente);
        ResponseEntity<Cliente> response = clienteController.update(1, cliente);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testUpdateNoExistente() {
        when(clienteService.update(eq(2), any(Cliente.class))).thenReturn(null);

        ResponseEntity<Cliente> response = clienteController.update(2, cliente);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testDelete() {
        doNothing().when(clienteService).delete(1);
        ResponseEntity<Void> response = clienteController.delete(1);
        assertEquals(204, response.getStatusCode().value());
        verify(clienteService, times(1)).delete(1);
    }
}
