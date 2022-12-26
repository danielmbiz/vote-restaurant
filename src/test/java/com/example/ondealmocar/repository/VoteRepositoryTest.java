package com.example.ondealmocar.repository;

import com.example.ondealmocar.dto.VoteWinWeek;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.enums.VoteStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DataJpaTest
public class VoteRepositoryTest {
    @Autowired
    private VoteRepository repository;
    @Autowired
    TestEntityManager entity;

    public final static LocalDate DATE_VOTE = LocalDate.parse("2022-12-19");
    public final static Vote INVALID_VOTE = new Vote(null, null, null, null);
    public final static Vote EMPTY_VOTE = new Vote();

    @Test
    public void createVote_WithValidData_ReturnsVote() {
        Vote voteIns = new Vote(null, DATE_VOTE, VoteStatus.OPEN, null);
        Vote vote = repository.save(voteIns);
        Vote sut = entity.find(Vote.class, vote.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(voteIns.getId());
        assertThat(sut.getDateVote()).isEqualTo(voteIns.getDateVote());
        assertThat(sut.getStatus()).isEqualTo(voteIns.getStatus());
        assertThat(sut.getRestaurantWin()).isEqualTo(voteIns.getRestaurantWin());
    }

    @Test
    public void createVote_WithInvalidData_ThrowsException() {
        assertThatThrownBy(() -> repository.save(INVALID_VOTE)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> repository.save(EMPTY_VOTE)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createVote_WithExistingName_ThrowsException() {
        Vote voteIns = new Vote(null, DATE_VOTE, VoteStatus.OPEN, null);
        Vote vote = entity.persistFlushFind(voteIns);
        entity.detach(vote);
        vote.setId(null);

        assertThatThrownBy(() -> repository.save(vote)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findByIdVote_ByExistingId_ReturnsVote() {
        Vote voteIns = new Vote(null, DATE_VOTE, VoteStatus.OPEN, null);
        Vote vote = entity.persistFlushFind(voteIns);
        Optional<Vote> sut = repository.findById(vote.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(vote);
    }

    @Test
    public void findByIdVote_ByUnexistingId_ReturnsEmpty() {
        Optional<Vote> sut = repository.findById(999L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void findByWinWeekVote_ByExistingId_ReturnsListVoteWinWeek() {
        Restaurant restaurantIns = new Restaurant(null, "nome");
        Restaurant restaurant = entity.persistFlushFind(restaurantIns);
        entity.detach(restaurant);
        Vote voteIns = new Vote(null, DATE_VOTE, VoteStatus.CLOSE, restaurant);
        Vote vote = entity.persistFlushFind(voteIns);

        List<VoteWinWeek> list = new ArrayList<>();
        list.add(new VoteWinWeek(DATE_VOTE, restaurant));
        var dateVoteIni = DATE_VOTE.minusDays(DATE_VOTE.getDayOfWeek().getValue());
        var dateVoteEnd = dateVoteIni.plusDays(6);
        List<VoteWinWeek> sut = repository.findByWinWeek(dateVoteIni, dateVoteEnd, VoteStatus.CLOSE);

        assertThat(sut).asList().isNotEmpty();
        assertThat(sut.get(0).getClass()).isEqualTo(list.get(0).getClass());
    }

    @Test
    public void findByWinWeekVote_ByUnexistingId_ReturnsEmpty() {
        var dateVoteIni = DATE_VOTE.minusDays(DATE_VOTE.getDayOfWeek().getValue());
        var dateVoteEnd = dateVoteIni.plusDays(6);
        List<VoteWinWeek> sut = repository.findByWinWeek(dateVoteIni, dateVoteEnd, VoteStatus.CLOSE);

        assertThat(sut).asList().isEmpty();
    }

    @Test
    public void findByWinRestaurantIdWeekVote_ByExistingId_ReturnsListVoteWinWeek() {
        Restaurant restaurantIns = new Restaurant(null, "nome");
        Restaurant restaurant = entity.persistFlushFind(restaurantIns);
        entity.detach(restaurant);
        Vote voteIns = new Vote(null, DATE_VOTE, VoteStatus.OPEN, restaurant);
        Vote vote = entity.persistFlushFind(voteIns);

        List<Vote> list = new ArrayList<>();
        list.add(voteIns);
        var dateVoteIni = DATE_VOTE.minusDays(DATE_VOTE.getDayOfWeek().getValue());
        var dateVoteEnd = dateVoteIni.plusDays(6);
        List<Vote> sut = repository.findByWinRestaurantIdWeek(restaurant.getId(), dateVoteIni, dateVoteEnd);

        assertThat(sut).asList().isNotEmpty();
        assertThat(sut.get(0).getClass()).isEqualTo(list.get(0).getClass());
    }

    @Test
    public void findByWinRestaurantIdWeekVote_ByUnexistingId_ReturnsEmpty() {
        Restaurant restaurantIns = new Restaurant(null, "nome");
        Restaurant restaurant = entity.persistFlushFind(restaurantIns);
        entity.detach(restaurant);
        var dateVoteIni = DATE_VOTE.minusDays(DATE_VOTE.getDayOfWeek().getValue());
        var dateVoteEnd = dateVoteIni.plusDays(6);
        List<Vote> sut = repository.findByWinRestaurantIdWeek(restaurant.getId(), dateVoteIni, dateVoteEnd);

        assertThat(sut).asList().isEmpty();
    }
}
