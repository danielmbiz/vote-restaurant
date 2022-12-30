package com.example.ondealmocar.service;

import com.example.ondealmocar.dto.VoteDTO;
import com.example.ondealmocar.dto.VoteItemWin;
import com.example.ondealmocar.dto.VoteWinWeek;
import com.example.ondealmocar.exception.DatabaseException;
import com.example.ondealmocar.exception.ResourceNotFoundException;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.enums.VoteStatus;
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
public class VoteServiceTest {

    public final static LocalDate DATE_VOTE = LocalDate.parse("2022-12-19");
    public static Vote vote = new Vote(1L, DATE_VOTE, VoteStatus.OPEN, null);
    public final static Vote INVALID_VOTE = new Vote(null, null, null, null);
    public final static Restaurant RESTAURANT = new Restaurant(null, "nome");

    @InjectMocks
    private VoteService service;

    @Mock
    private VoteRepository repository;

    @Mock
    private VoteItemRepository voteItemRepository;

    @Test
    public void findByIdVote_WithValidData_ReturnsVoteDTO() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(vote));
        var dto = VoteDTO.of(vote);

        VoteDTO sut = service.findById(1L);

        assertEquals(VoteDTO.class, sut.getClass());
        assertNotNull(sut);
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getDateVote(), sut.getDateVote());
        assertEquals(dto.getStatus(), sut.getStatus());
        assertEquals(dto.getRestaurantWin(), sut.getRestaurantWin());
    }

    @Test
    public void findByIdVote_WithInvalidData_ReturnsResourceNotFound() {
        when(repository.findById(anyLong())).thenThrow(new ResourceNotFoundException(""));

        try {
            service.findById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
        }
    }

    @Test
    public void findByAllVote_WithValidData_ReturnsListVoteDTO() {
        List<Vote> list = new ArrayList<>();
        list.add(vote);
        VoteDTO dto = new VoteDTO(vote);
        when(repository.findAll()).thenReturn(list);

        List<VoteDTO> sut = service.findAll();

        assertThat(sut).asList().isNotEmpty();
        assertThat(sut).asList().hasSize(1);
        assertEquals(VoteDTO.class, sut.get(0).getClass());
        assertEquals(dto.getId(), sut.get(0).getId());
        assertEquals(dto.getDateVote(), sut.get(0).getDateVote());
        assertEquals(dto.getStatus(), sut.get(0).getStatus());
        assertEquals(dto.getRestaurantWin(), sut.get(0).getRestaurantWin());
    }

    @Test
    public void findByWinWeekVote_WithValidData_ReturnsListVoteDTO() {
        List<VoteWinWeek> list = new ArrayList<>();
        list.add(new VoteWinWeek(DATE_VOTE, RESTAURANT));
        var dateVoteIni = DATE_VOTE.minusDays(DATE_VOTE.getDayOfWeek().getValue());
        var dateVoteEnd = dateVoteIni.plusDays(6);

        when(repository.findByWinWeek(dateVoteIni, dateVoteEnd, VoteStatus.CLOSE)).thenReturn(list);
        VoteWinWeek dto = new VoteWinWeek(DATE_VOTE, RESTAURANT);

        List<VoteWinWeek> sut = service.findByWinWeek("2022-12-19");

        assertThat(sut).asList().isNotEmpty();
        assertThat(sut).asList().hasSize(1);
        assertEquals(VoteWinWeek.class, sut.get(0).getClass());
        assertEquals(dto.getDateVote(), sut.get(0).getDateVote());
        assertEquals(dto.getRestaurantWin(), sut.get(0).getRestaurantWin());
    }

    @Test
    public void createVote_WithValidData_ReturnsVoteDTO() {
        when(repository.save(any())).thenReturn(vote);
        var dto = VoteDTO.of(vote);

        VoteDTO sut = service.save(dto);

        assertNotNull(sut);
        assertEquals(VoteDTO.class, sut.getClass());
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getDateVote(), sut.getDateVote());
        assertEquals(dto.getStatus(), sut.getStatus());
        assertEquals(dto.getRestaurantWin(), sut.getRestaurantWin());
    }

    @Test
    public void createVote_DataIntegraty_RuntimeException() {
        when(repository.save(any())).thenThrow(RuntimeException.class);
        var dto = VoteDTO.of(INVALID_VOTE);

        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createVote_DataIntegraty_DataIntegrityViolationReturnDatabaseException() {
        when(repository.save(any())).thenThrow(DataIntegrityViolationException.class);
        var dto = VoteDTO.of(INVALID_VOTE);

        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(DatabaseException.class);
    }

    @Test
    public void createVote_EmailDataIntegraty_DataIntegrityViolationException() {
        when(repository.save(any())).thenReturn(vote);

        try {
            vote.setId(1L);
            var dto = VoteDTO.of(vote);
            service.save(dto);
        } catch (DataIntegrityViolationException e) {
            assertEquals(DatabaseException.class, e.getClass());
        }
    }

    @Test
    public void finishVote_WithValidData_ReturnsVoteDTO() {
        List<VoteItemWin> list = new ArrayList<>();
        list.add(new VoteItemWin(DATE_VOTE, 1L, RESTAURANT));
        when(repository.findById(anyLong())).thenReturn(Optional.of(vote));
        when(repository.save(any())).thenReturn(vote);
        when(voteItemRepository.findByWinDay(vote.getDateVote(), VoteStatus.OPEN)).thenReturn(list);

        var dto = VoteDTO.of(vote);
        VoteDTO sut = service.finish(1L);

        assertNotNull(sut);
        assertEquals(VoteDTO.class, sut.getClass());
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getDateVote(), sut.getDateVote());
        assertEquals(dto.getStatus(), sut.getStatus());
        assertEquals(dto.getRestaurantWin(), sut.getRestaurantWin());
    }

    @Test
    public void finishVote_DataIntegraty_RuntimeException() {
        assertThatThrownBy(() -> service.finish(anyLong())).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void deleteVote_WithValidData_doesNotThrowsException() {
        assertThatCode(() -> service.delete(anyLong())).doesNotThrowAnyException();
    }

    @Test
    public void deleteVote_DataIntegraty_DatabaseException() {
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());
        assertThatThrownBy(() -> service.delete(anyLong())).isInstanceOf(DatabaseException.class);
    }

    @Test
    public void deleteVote_DataIntegraty_EmptyResultDataAccessException() {
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());
        assertThatThrownBy(() -> service.delete(anyLong())).isInstanceOf(ResourceNotFoundException.class);
    }

}
