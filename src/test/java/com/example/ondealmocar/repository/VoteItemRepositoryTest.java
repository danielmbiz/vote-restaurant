package com.example.ondealmocar.repository;

import com.example.ondealmocar.dto.VoteItemWin;
import com.example.ondealmocar.dto.VoteWinWeek;
import com.example.ondealmocar.model.Employee;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.VoteItem;
import com.example.ondealmocar.model.enums.VoteStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
public class VoteItemRepositoryTest {

    public final static LocalDate DATE_VOTE = LocalDate.parse("2022-12-19");
    public final static Vote VOTE_EMPTY = new Vote();
    public final static Employee EMPLOYEE_EMPTY = new Employee();
    public final static Restaurant RESTAURANT_EMPTY = new Restaurant();
    public final static VoteItem INVALID_VOTE_ITEM = new VoteItem((Long) null, null, null, null);
    public final static VoteItem EMPTY_VOTE_ITEM = new VoteItem();

    @Autowired
    private VoteItemRepository repository;

    @Autowired
    TestEntityManager entity;

    @Test
    public void createVoteItem_WithValidData_ReturnsVoteItem() {
        Vote vote = new Vote(null, DATE_VOTE, VoteStatus.OPEN, null);
        entity.persistFlushFind(vote);
        entity.detach(vote);
        Employee employee = new Employee(null, "nome", "email@email");
        entity.persistFlushFind(employee);
        entity.detach(employee);
        Restaurant restaurant = new Restaurant(null, "nome");
        entity.persistFlushFind(restaurant);
        entity.detach(restaurant);

        VoteItem voteItemIns = new VoteItem((Long) null, vote, employee, restaurant);
        VoteItem voteItem = repository.save(voteItemIns);
        VoteItem sut = entity.find(VoteItem.class, voteItem.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(voteItem.getId());
        assertThat(sut.getVote()).isEqualTo(voteItem.getVote());
        assertThat(sut.getEmployee()).isEqualTo(voteItem.getEmployee());
        assertThat(sut.getRestaurant()).isEqualTo(voteItem.getRestaurant());
    }

    @Test
    public void createVoteItem_WithInvalidData_ThrowsException() {
        assertThatThrownBy(() -> repository.save(INVALID_VOTE_ITEM)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> repository.save(EMPTY_VOTE_ITEM)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findByIdVoteItem_ByExistingId_ReturnsVoteItem() {
        Vote vote = new Vote(null, DATE_VOTE, VoteStatus.OPEN, null);
        entity.persistFlushFind(vote);
        entity.detach(vote);
        Employee employee = new Employee(null, "nome", "email@email");
        entity.persistFlushFind(employee);
        entity.detach(employee);
        Restaurant restaurant = new Restaurant(null, "nome");
        entity.persistFlushFind(restaurant);
        entity.detach(restaurant);

        VoteItem voteItemIns = new VoteItem((Long) null, vote, employee, restaurant);
        VoteItem voteItem = entity.persistFlushFind(voteItemIns);
        Optional<VoteItem> sut = repository.findById(voteItem.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(voteItem);
    }

    @Test
    public void findByIdVoteItem_ByUnexistingId_ReturnsEmpty() {
        Optional<VoteItem> sut = repository.findById(999L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void findByEmployeeDayVoteItem_ByExistingId_ReturnsVoteItem() {
        Vote vote = new Vote(null, DATE_VOTE, VoteStatus.OPEN, null);
        entity.persistFlushFind(vote);
        entity.detach(vote);
        Employee employee = new Employee(null, "nome", "email@email");
        entity.persistFlushFind(employee);
        entity.detach(employee);
        Restaurant restaurant = new Restaurant(null, "nome");
        entity.persistFlushFind(restaurant);
        entity.detach(restaurant);

        VoteItem voteItemIns = new VoteItem((Long) null, vote, employee, restaurant);
        VoteItem voteItem = entity.persistFlushFind(voteItemIns);

        VoteItem sut = repository.findByEmployeeDay(employee.getId(), DATE_VOTE);

        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(voteItem.getId());
        assertThat(sut.getVote()).isEqualTo(voteItem.getVote());
        assertThat(sut.getEmployee()).isEqualTo(voteItem.getEmployee());
        assertThat(sut.getRestaurant()).isEqualTo(voteItem.getRestaurant());
    }

    @Test
    public void findByEmployeeDayVoteItem_ByUnexistingId_ReturnsEmpty() {
        Employee employee = new Employee(null, "nome", "email@email");
        entity.persistFlushFind(employee);
        entity.detach(employee);
        var dateVoteIni = DATE_VOTE.minusDays(DATE_VOTE.getDayOfWeek().getValue());
        var dateVoteEnd = dateVoteIni.plusDays(6);

        VoteItem sut = repository.findByEmployeeDay(employee.getId(), DATE_VOTE);

        assertThat(sut).isNull();
    }

    @Test
    public void findByWinDayVoteItem_ByExistingId_ReturnsVoteItem() {
        Vote vote = new Vote(null, DATE_VOTE, VoteStatus.CLOSE, null);
        entity.persistFlushFind(vote);
        entity.detach(vote);
        Employee employee = new Employee(null, "nome", "email@email");
        entity.persistFlushFind(employee);
        entity.detach(employee);
        Restaurant restaurant = new Restaurant(null, "nome");
        entity.persistFlushFind(restaurant);
        entity.detach(restaurant);

        VoteItem voteItemIns = new VoteItem((Long) null, vote, employee, restaurant);
        VoteItem voteItem = entity.persistFlushFind(voteItemIns);

        List<VoteItemWin> list = new ArrayList<>();
        list.add(new VoteItemWin(DATE_VOTE, 1L, restaurant));
        List<VoteItemWin> sut = repository.findByWinDay(DATE_VOTE, VoteStatus.CLOSE);

        assertThat(sut).asList().isNotEmpty();
        assertThat(sut.get(0).getClass()).isEqualTo(list.get(0).getClass());
    }

    @Test
    public void findByWinDayVoteItem_ByUnexistingId_ReturnsEmpty() {
        List<VoteItemWin> list = repository.findByWinDay(DATE_VOTE, VoteStatus.CLOSE);
        assertThat(list).asList().isEmpty();
    }

}
