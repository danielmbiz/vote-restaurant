package com.example.ondealmocar.controller;

import com.example.ondealmocar.dto.EmployeeDTO;
import com.example.ondealmocar.dto.VoteDTO;
import com.example.ondealmocar.dto.VoteWinWeek;
import com.example.ondealmocar.model.Restaurant;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
public class VoteControllerTest {

    public final static Restaurant RESTAURANT = new Restaurant(1L, "Restaurante");
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
                .andExpect(jsonPath("$.id").value(VOTE_DTO.getId()))
                .andExpect(jsonPath("$.dateVote").value(VOTE_DTO.getDateVote().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.status").value(VOTE_DTO.getStatus().name()))
                .andExpect(jsonPath("$.restaurantWin").value(VOTE_DTO.getRestaurantWin()));

    }

    @Test
    public void createVote_WithInvalidData_ReturnsBadRequest() throws Exception {
        mvc.perform(post("/votes")
                        .content(mapper.writeValueAsString(INVALID_VOTE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void finishVote_WithValidData_ReturnsOk() throws Exception {
        when(service.finish(any())).thenReturn(VOTE_DTO);

        mvc.perform(put("/votes/finish/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOTE_DTO.getId()))
                .andExpect(jsonPath("$.dateVote").value(VOTE_DTO.getDateVote().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.status").value(VOTE_DTO.getStatus().name()))
                .andExpect(jsonPath("$.restaurantWin").value(VOTE_DTO.getRestaurantWin()));

    }

    @Test
    public void getVote_ByExistingId_ReturnsVote() throws Exception {
        when(service.findById(1L)).thenReturn(VOTE_DTO);

        mvc.perform(get("/votes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOTE_DTO.getId()))
                .andExpect(jsonPath("$.dateVote").value(VOTE_DTO.getDateVote().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.status").value(VOTE_DTO.getStatus().name()))
                .andExpect(jsonPath("$.restaurantWin").value(VOTE_DTO.getRestaurantWin()));
    }

    @Test
    public void getVote_ByUnexistingId_ReturnsNotFound() throws Exception {
        mvc.perform(get("/votes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getVote_ByFindAll_ReturnsListVote() throws Exception {
        List<VoteDTO> list = new ArrayList<>();
        list.add(VOTE_DTO);
        when(service.findAll()).thenReturn(list);

        mvc.perform(get("/votes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(VOTE_DTO.getId()))
                .andExpect(jsonPath("$[0].dateVote").value(VOTE_DTO.getDateVote().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].status").value(VOTE_DTO.getStatus().name()))
                .andExpect(jsonPath("$[0].restaurantWin").value(VOTE_DTO.getRestaurantWin()));
    }

    @Test
    public void getVote_ByWinWeek_ReturnsVote() throws Exception {
        VoteWinWeek voteWin = new VoteWinWeek(DATE_VOTE, RESTAURANT);
        List<VoteWinWeek> list = new ArrayList<>();
        list.add(voteWin);
        when(service.findByWinWeek("2022-12-23")).thenReturn(list);

        mvc.perform(get("/votes/vote/win/week/2022-12-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dateVote").value(list.get(0).getDateVote().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].restaurantWin.id").value(list.get(0).getRestaurantWin().getId()));
    }

    @Test
    public void deleteVote_WithValidData_ReturnsNoContent() throws Exception {
        mvc.perform(delete("/votes/1"))
                .andExpect(status().isNoContent());
    }
}
