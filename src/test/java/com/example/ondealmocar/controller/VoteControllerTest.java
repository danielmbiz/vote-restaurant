package com.example.ondealmocar.controller;

import com.example.ondealmocar.dto.VoteDTO;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.enums.VoteStatus;
import com.example.ondealmocar.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
public class VoteControllerTest {

    public final static LocalDate DATE_VOTE = LocalDate.parse("2022-12-23");
    public final static VoteDTO VOTE_DTO = new VoteDTO(1L, DATE_VOTE, VoteStatus.OPEN, null);
    public final static Vote INVALID_VOTE = new Vote(null, null, null, null);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VoteService service;

    @Test
    public void createVote_WithValidData_ReturnsCreated() throws Exception {
        when(service.save(any())).thenReturn(VOTE_DTO);

        mvc.perform(post("/votes")
                        .content(mapper.writeValueAsString(VOTE_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(VOTE_DTO.getId()));
    }

    @Test
    public void createVote_WithInvalidData_ReturnsBadRequest() throws Exception {
        mvc.perform(post("/votes")
                        .content(mapper.writeValueAsString(INVALID_VOTE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }
}
