package com.example.ondealmocar.controller;

import com.example.ondealmocar.dto.EmployeeDTO;
import com.example.ondealmocar.service.EmployeeService;
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

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    public final static EmployeeDTO EMPLOYEE_DTO = new EmployeeDTO(null, "nome", "email@email");
    public final static EmployeeDTO INVALID_EMPLOYEE_DTO = new EmployeeDTO(1L, "", "");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private EmployeeService service;

    @Test
    public void createEmployee_WithValidData_ReturnsCreated() throws Exception {
        when(service.save(any())).thenReturn(EMPLOYEE_DTO);

        mvc.perform(post("/employees")
                        .content(mapper.writeValueAsString(EMPLOYEE_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(EMPLOYEE_DTO.getId()))
                .andExpect(jsonPath("$.name").value(EMPLOYEE_DTO.getName()))
                .andExpect(jsonPath("$.email").value(EMPLOYEE_DTO.getEmail()));
    }

    @Test
    public void getEmployee_ByExistingId_ReturnsEmployee() throws Exception {
        when(service.findById(1L)).thenReturn(EMPLOYEE_DTO);

        mvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EMPLOYEE_DTO.getId()))
                .andExpect(jsonPath("$.name").value(EMPLOYEE_DTO.getName()))
                .andExpect(jsonPath("$.email").value(EMPLOYEE_DTO.getEmail()));
    }

    @Test
    public void deleteEmployee_WithValidData_ReturnsNoContent() throws Exception {
        mvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getEmployee_ByFindAll_ReturnsListEmployee() throws Exception {
        List<EmployeeDTO> list = new ArrayList<>();
        list.add(EMPLOYEE_DTO);
        when(service.findAll()).thenReturn(list);

        mvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(list.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(list.get(0).getName()))
                .andExpect(jsonPath("$[0].email").value(list.get(0).getEmail()));
    }

    @Test
    public void updateEmployee_WithValidData_ReturnsOk() throws Exception {
        when(service.update(anyLong(), any())).thenReturn(EMPLOYEE_DTO);

        mvc.perform(put("/employees/1")
                        .content(mapper.writeValueAsString(EMPLOYEE_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EMPLOYEE_DTO.getId()))
                .andExpect(jsonPath("$.name").value(EMPLOYEE_DTO.getName()))
                .andExpect(jsonPath("$.email").value(EMPLOYEE_DTO.getEmail()));
    }

}
