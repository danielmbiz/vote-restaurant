package com.example.ondealmocar.controller;

import com.example.ondealmocar.dto.EmployeeDTO;
import com.example.ondealmocar.dto.RestaurantDTO;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    public final static RestaurantDTO RESTAURANT_DTO = new RestaurantDTO(null, "Restaurante");
    public final static RestaurantDTO INVALID_RESTAURANT_DTO = new RestaurantDTO(null, "");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RestaurantService service;

    @Test
    public void createRestaurant_WithValidData_ReturnsRestaurant() throws Exception {
        when(service.save(any())).thenReturn(RESTAURANT_DTO);

        mvc.perform(post("/restaurants")
                        .content(mapper.writeValueAsString(RESTAURANT_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(RESTAURANT_DTO.getId()))
                .andExpect(jsonPath("$.name").value(RESTAURANT_DTO.getName()));
    }

    @Test
    public void createRestaurant_WithInvalidData_ReturnsBadRequest() throws Exception {
        mvc.perform(post("/restaurants")
                        .content(mapper.writeValueAsString(INVALID_RESTAURANT_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getRestaurant_ByExistingId_ReturnsRestaurant() throws Exception {
        when(service.findById(1L)).thenReturn(RESTAURANT_DTO);

        mvc.perform(get("/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(RESTAURANT_DTO.getId()))
                .andExpect(jsonPath("$.name").value(RESTAURANT_DTO.getName()));
    }

    @Test
    public void getRestaurant_ByUnexistingId_ReturnsNotFound() throws Exception {
        mvc.perform(get("/restaurants/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteRestaurant_WithValidData_ReturnsNoContent() throws Exception {
        mvc.perform(delete("/restaurants/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getRestaurant_ByFindAll_ReturnsListRestaurant() throws Exception {
        List<RestaurantDTO> list = new ArrayList<>();
        list.add(RESTAURANT_DTO);
        when(service.findAll()).thenReturn(list);

        mvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(list.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(list.get(0).getName()));
    }

    @Test
    public void updateRestaurant_WithValidData_ReturnsOk() throws Exception {
        when(service.update(anyLong(), any())).thenReturn(RESTAURANT_DTO);

        mvc.perform(put("/restaurants/1")
                        .content(mapper.writeValueAsString(RESTAURANT_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(RESTAURANT_DTO.getId()))
                .andExpect(jsonPath("$.name").value(RESTAURANT_DTO.getName()));
    }
}
