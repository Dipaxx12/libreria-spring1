package com.distribuida.controller;

import com.distribuida.model.Carrito;
import com.distribuida.service.CarritoService;
import com.distribuida.service.GuestCheckoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarritoGuestController.class)
class CarritoGuestControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CarritoService carritoService;

    @MockBean
    GuestCheckoutService guestCheckoutService;

    @Test
    void addItem_porToken_devuelve200() throws Exception {
        when(carritoService.addItem(eq("ABC"), anyInt(), anyInt()))
                .thenReturn(new Carrito());

        mvc.perform(post("/api/guest/cart/items")
                .param("token", "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"libroId\": 1, \"cantidad\": 2}"))
                .andExpect(status().isOk());
    }
}
