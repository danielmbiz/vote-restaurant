package com.example.ondealmocar.controller;

import com.example.ondealmocar.dto.VoteDTO;
import com.example.ondealmocar.dto.VoteItemResponse;
import com.example.ondealmocar.dto.VoteItemWin;
import com.example.ondealmocar.dto.VoteWinWeek;
import com.example.ondealmocar.model.Employee;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.VoteItem;
import com.example.ondealmocar.model.enums.VoteStatus;
import com.example.ondealmocar.service.VoteItemService;
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

@WebMvcTest(VoteItemController.class)
public class VoteItemControllerTest {

    public final static LocalDate DATE_VOTE = LocalDate.parse("2022-12-23");
    public final static Vote VOTE = new Vote(1L, DATE_VOTE, VoteStatus.OPEN, null);
    public final static Employee EMPLOYEE = new Employee(1L, "nome", "email@email");
    public final static Restaurant RESTAURANT = new Restaurant(1L, "nome");
    public static VoteItemResponse VOTE_ITEM_RESPONSE = new VoteItemResponse(1L, VOTE, EMPLOYEE, RESTAURANT);
    public final static Vote VOTE_NULL = new Vote();
    public final static Employee EMPLOYEE_NULL = new Employee();
    public final static Restaurant RESTAURANT_NULL = new Restaurant();
    public final static VoteItemResponse INVALID_VOTE_ITEM_RESPONSE = new VoteItemResponse(1L, VOTE_NULL, EMPLOYEE_NULL, RESTAURANT_NULL);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VoteItemService service;

    @Test
    public void createVoteItem_WithValidData_ReturnsCreated() throws Exception {
        when(service.save(any())).thenReturn(VOTE_ITEM_RESPONSE);

        mvc.perform(post("/votes/vote")
                        .content(mapper.writeValueAsString(VOTE_ITEM_RESPONSE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(VOTE_ITEM_RESPONSE.getId()))
                .andExpect(jsonPath("$.vote.id").value(VOTE_ITEM_RESPONSE.getVote().getId()))
                .andExpect(jsonPath("$.employee.id").value(VOTE_ITEM_RESPONSE.getEmployee().getId()))
                .andExpect(jsonPath("$.restaurant.id").value(VOTE_ITEM_RESPONSE.getRestaurant().getId()));

    }

    @Test
    public void createVoteItem_WithInvalidData_ReturnsBadRequest() throws Exception {
        mvc.perform(post("/votes/vote")
                        .content(mapper.writeValueAsString(INVALID_VOTE_ITEM_RESPONSE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getVoteItem_ByExistingId_ReturnsVoteItem() throws Exception {
        when(service.findById(1L)).thenReturn(VOTE_ITEM_RESPONSE);

        mvc.perform(get("/votes/vote/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOTE_ITEM_RESPONSE.getId()))
                .andExpect(jsonPath("$.vote.id").value(VOTE_ITEM_RESPONSE.getVote().getId()))
                .andExpect(jsonPath("$.employee.id").value(VOTE_ITEM_RESPONSE.getEmployee().getId()))
                .andExpect(jsonPath("$.restaurant.id").value(VOTE_ITEM_RESPONSE.getRestaurant().getId()));
    }

    @Test
    public void getVoteItem_ByUnexistingId_ReturnsNotFound() throws Exception {
        mvc.perform(get("/votes/vote/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getVoteItem_ByFindAll_ReturnsListVoteItem() throws Exception {
        List<VoteItemResponse> list = new ArrayList<>();
        list.add(VOTE_ITEM_RESPONSE);
        when(service.findAll()).thenReturn(list);

        mvc.perform(get("/votes/vote"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(VOTE_ITEM_RESPONSE.getId()))
                .andExpect(jsonPath("$[0].vote.id").value(VOTE_ITEM_RESPONSE.getVote().getId()))
                .andExpect(jsonPath("$[0].employee.id").value(VOTE_ITEM_RESPONSE.getEmployee().getId()))
                .andExpect(jsonPath("$[0].restaurant.id").value(VOTE_ITEM_RESPONSE.getRestaurant().getId()));
    }

    @Test
    public void getVoteItem_ByWinDay_ReturnsVoteItemWin() throws Exception {
        VoteItemWin voteWin = new VoteItemWin(DATE_VOTE, 10L, RESTAURANT);
        when(service.findByWinDay("2022-12-23", VoteStatus.CLOSE)).thenReturn(voteWin);

        mvc.perform(get("/votes/vote/win/2022-12-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateVote").value(voteWin.getDateVote().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.quantityVote").value(voteWin.getQuantityVote()))
                .andExpect(jsonPath("$.restaurant.id").value(voteWin.getRestaurant().getId()))
                .andExpect(jsonPath("$.dayWeek").value(voteWin.getDayWeek()));
    }

    @Test
    public void deleteVoteItem_WithValidData_ReturnsNoContent() throws Exception {
        mvc.perform(delete("/votes/vote/1"))
                .andExpect(status().isNoContent());
    }
}
