package com.example.ondealmocar.service;


import com.example.ondealmocar.dto.VoteItemRequest;
import com.example.ondealmocar.dto.VoteItemResponse;
import com.example.ondealmocar.dto.VoteItemWin;
import com.example.ondealmocar.exception.DatabaseException;
import com.example.ondealmocar.exception.ResourceNotFoundException;
import com.example.ondealmocar.exception.ValidationException;
import com.example.ondealmocar.model.Employee;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.VoteItem;
import com.example.ondealmocar.model.enums.VoteStatus;
import com.example.ondealmocar.repository.EmployeeRepository;
import com.example.ondealmocar.repository.RestaurantRepository;
import com.example.ondealmocar.repository.VoteItemRepository;
import com.example.ondealmocar.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoteItemServiceTest {

    public final static LocalDate DATE_VOTE = LocalDate.parse("2022-12-23");
    public final static Vote VOTE = new Vote(1L, DATE_VOTE, VoteStatus.OPEN, null);
    public final static Employee EMPLOYEE = new Employee(1L, "nome", "email@email");
    public final static Restaurant RESTAURANT = new Restaurant(1L, "nome");
    public static VoteItem voteItem = new VoteItem(1L, VOTE, EMPLOYEE, RESTAURANT);
    public final static Vote VOTE_NULL = new Vote();
    public final static Employee EMPLOYEE_NULL = new Employee();
    public final static Restaurant RESTAURANT_NULL = new Restaurant();
    public final static VoteItem INVALID_VOTE_ITEM = new VoteItem(1L, VOTE_NULL, EMPLOYEE_NULL, RESTAURANT_NULL);

    @InjectMocks
    private VoteItemService service;

    @Mock
    private VoteItemRepository repository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    public void findByIdVoteItem_WithValidData_ReturnsVoteItemResponse() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(voteItem));
        var dto = VoteItemResponse.of(voteItem);

        VoteItemResponse sut = service.findById(1L);

        assertEquals(VoteItemResponse.class, sut.getClass());
        assertNotNull(sut);
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getVote(), sut.getVote());
        assertEquals(dto.getRestaurant(), sut.getRestaurant());
        assertEquals(dto.getEmployee(), sut.getEmployee());
    }

    @Test
    public void findByIdVoteItem_WithInvalidData_ReturnsResourceNotFound() {
        when(repository.findById(anyLong())).thenThrow(new ResourceNotFoundException(""));

        try {
            service.findById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
        }
    }

    @Test
    public void findByAllVoteItem_WithValidData_ReturnsListVoteItemResponse() {
        List<VoteItem> list = new ArrayList<>();
        list.add(voteItem);
        VoteItemResponse dto = new VoteItemResponse(voteItem);
        when(repository.findAll()).thenReturn(list);

        List<VoteItemResponse> sut = service.findAll();

        assertThat(sut).asList().isNotEmpty();
        assertThat(sut).asList().hasSize(1);
        assertEquals(VoteItemResponse.class, sut.get(0).getClass());
        assertEquals(dto.getId(), sut.get(0).getId());
        assertEquals(dto.getVote(), sut.get(0).getVote());
        assertEquals(dto.getEmployee(), sut.get(0).getEmployee());
        assertEquals(dto.getRestaurant(), sut.get(0).getRestaurant());
    }

    @Test
    public void findByWinDayVoteItem_WithValidData_ReturnsVoteItemWin() {
        List<VoteItemWin> list = new ArrayList<>();
        list.add(new VoteItemWin(DATE_VOTE, 1L, RESTAURANT));
        when(repository.findByWinDay(DATE_VOTE, VoteStatus.CLOSE)).thenReturn(list);
        var test = new VoteItemWin(list.get(0).getDateVote(), list.get(0).getQuantityVote(), list.get(0).getRestaurant());

        var sut = service.findByWinDay("2022-12-23", VoteStatus.CLOSE);

        assertEquals(VoteItemWin.class, sut.getClass());
        assertNotNull(sut);
        assertEquals(test.getDayWeek(), sut.getDayWeek());
        assertEquals(test.getDateVote(), sut.getDateVote());
        assertEquals(test.getQuantityVote(), sut.getQuantityVote());
        assertEquals(test.getRestaurant(), sut.getRestaurant());
    }

    @Test
    public void findByWinDayVoteItem_WithInvalidData_ReturnsValidationException() {
        assertThatThrownBy(() -> service.findByWinDay("DATA ERRADA", VoteStatus.CLOSE))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void createVoteItem_WithValidData_ReturnsVoteItemResponse() {
        when(voteRepository.findById(1L)).thenReturn(Optional.of(VOTE));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(EMPLOYEE));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(RESTAURANT));
        when(repository.save(any())).thenReturn(voteItem);

        var voteItemResponse = VoteItemResponse.of(voteItem);
        var dto = new VoteItemRequest(voteItem.getId(),
                voteItem.getVote().getId(),
                voteItem.getEmployee().getId(),
                voteItem.getRestaurant().getId());

        var sut = service.save(dto);

        assertNotNull(sut);
        assertEquals(VoteItemResponse.class, sut.getClass());
        assertEquals(voteItemResponse.getId(), sut.getId());
        assertEquals(voteItemResponse.getVote(), sut.getVote());
        assertEquals(voteItemResponse.getEmployee(), sut.getEmployee());
        assertEquals(voteItemResponse.getRestaurant(), sut.getRestaurant());
    }

    @Test
    public void createVoteItem_DataIntegraty_ValidationException() {
        var dto = new VoteItemRequest(INVALID_VOTE_ITEM.getId(),
                INVALID_VOTE_ITEM.getVote().getId(),
                INVALID_VOTE_ITEM.getEmployee().getId(),
                INVALID_VOTE_ITEM.getRestaurant().getId());
        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(ValidationException.class);
    }

    @Test
    public void deleteVoteItem_WithValidData_doesNotThrowsException() {
        when(voteRepository.findById(1L)).thenReturn(Optional.of(VOTE));
        when(repository.findById(1L)).thenReturn(Optional.of(voteItem));

        assertThatCode(() -> service.delete(1L)).doesNotThrowAnyException();
    }

    @Test
    public void deleteVoteItem_DataIntegraty_DatabaseException() {
        when(voteRepository.findById(1L)).thenReturn(Optional.of(VOTE));
        when(repository.findById(1L)).thenReturn(Optional.of(voteItem));

        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(1L);
        assertThatThrownBy(() -> service.delete(1L)).isInstanceOf(DatabaseException.class);
    }

    @Test
    public void deleteVoteItem_DataIntegraty_EmptyResultDataAccessException() {
        when(voteRepository.findById(anyLong())).thenReturn(Optional.of(VOTE));
        when(repository.findById(anyLong())).thenReturn(Optional.of(voteItem));

        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(99L);
        assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(ResourceNotFoundException.class);
    }

}
