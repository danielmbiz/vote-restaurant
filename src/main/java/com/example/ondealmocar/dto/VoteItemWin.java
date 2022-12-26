package com.example.ondealmocar.dto;

import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.VoteItem;
import com.example.ondealmocar.model.enums.VoteStatus;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

public class VoteItemWin {

    private LocalDate dateVote;
    private String dayWeek;

    public Long getQuantityVote() {
        return quantityVote;
    }

    public void setQuantityVote(Long quantityVote) {
        this.quantityVote = quantityVote;
    }

    private Long quantityVote;
    private Restaurant restaurant;

    private VoteStatus status;

    public VoteItemWin() {

    }

    public VoteItemWin(LocalDate dateVote, Long quantityVote, Restaurant restaurant) {
        super();
        this.dateVote = dateVote;
        this.dayWeek = setDayWeek(dateVote);
        this.quantityVote = quantityVote;
        this.restaurant = restaurant;
    }

    private String setDayWeek(LocalDate dateVote) {
        return dateVote.getDayOfWeek().name();
    }

    public LocalDate getDateVote() {
        return dateVote;
    }

    public void setDateVote(LocalDate dateVote) {
        this.dateVote = dateVote;
    }

    public String getDayWeek() {
        return dayWeek;
    }

    public void setDayWeek(String dayWeek) {
        this.dayWeek = dayWeek;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public VoteStatus getStatus() {
        return status;
    }

    public void setStatus(VoteStatus status) {
        this.status = status;
    }

    public static VoteItemWin of(VoteItem voteItem) {
        var response = new VoteItemWin();
        BeanUtils.copyProperties(voteItem, response);
        return response;
    }

}
