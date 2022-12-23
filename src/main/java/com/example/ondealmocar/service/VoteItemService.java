package com.example.ondealmocar.service;

import com.example.ondealmocar.dto.VoteItemRequest;
import com.example.ondealmocar.dto.VoteItemResponse;
import com.example.ondealmocar.dto.VoteItemWin;
import com.example.ondealmocar.dto.projection.IVoteWin;
import com.example.ondealmocar.exception.DatabaseException;
import com.example.ondealmocar.exception.ResourceNotFoundException;
import com.example.ondealmocar.exception.ValidationException;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.VoteItem;
import com.example.ondealmocar.model.enums.VoteStatus;
import com.example.ondealmocar.repository.EmployeeRepository;
import com.example.ondealmocar.repository.RestaurantRepository;
import com.example.ondealmocar.repository.VoteItemRepository;
import com.example.ondealmocar.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteItemService {

    @Autowired
    private VoteItemRepository repository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public VoteItemResponse findById(Long id) {
        var vote = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Item de Voto não encontrado Id: " + id + " (Err. Vote Item Service: 01)"));
        return new VoteItemResponse(vote);

    }

    public VoteItemWin findByWinDay(String dateVoteString, VoteStatus status) {
        try {
            LocalDate dateVote = LocalDate.parse(dateVoteString);
            List<IVoteWin> list = repository.findByWinDay(dateVote, status);
            validateWinDay(list);
            var voteItemWin = new VoteItemWin(list.get(0).getDateVote(), list.get(0).getQuantityVote(),
                    list.get(0).getRestaurant());
            return voteItemWin;
        } catch (DateTimeParseException e) {
            throw new ValidationException("Data mal formatada");
        }
    }

    private void validateWinDay(List<IVoteWin> list) {
        if (list.isEmpty()) {
            throw new ValidationException("Não existe ganhador para o dia");
        }
    }

    public List<VoteItemResponse> findAll() {
        List<VoteItemResponse> list = repository.findAll().stream().map(x -> new VoteItemResponse(x))
                .collect(Collectors.toList());
        return list;
    }

    public VoteItemResponse save(VoteItemRequest request) {
        try {
            validateVoteInformed(request.getVoteId());
            var vote = voteRepository.findById(request.getVoteId()).get();
            voteClosed(vote.getStatus());
            voteDay(request.getEmployeeId(), vote.getDateVote());
            voteWinRestaurantIdWeek(request.getRestaurantId(), vote.getDateVote());
            validateEmployeeInformed(request.getEmployeeId());
            validateRestaurantInformed(request.getRestaurantId());
            var employee = employeeRepository.findById(request.getEmployeeId()).get();
            var restaurant = restaurantRepository.findById(request.getRestaurantId()).get();
            var voteItem = new VoteItemResponse(repository.save(new VoteItem(request, vote, employee, restaurant)));
            return voteItem;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Erro não definido");
        }
    }

    private void validateVoteInformed(Long voteId) {
        if ((voteId == null) || (voteId == 0)) {
            throw new ValidationException("Informar o Id do voto é obrigatório");
        }
    }

    private void validateRestaurantInformed(Long restaurantId) {
        if ((restaurantId == null) || (restaurantId == 0)) {
            throw new ValidationException("Informar o restaurante é obrigatório");
        }

    }

    private void validateEmployeeInformed(Long employeeId) {
        if ((employeeId == null) || (employeeId == 0)) {
            throw new ValidationException("Informar o profissional é obrigatório");
        }

    }

    private void voteClosed(VoteStatus status) {
        if (status == VoteStatus.CLOSE) {
            throw new ValidationException("Votação encerrada!");
        }
    }

    private void voteDay(Long employeeId, LocalDate dateVote) {
        var voteItem = repository.findByEmployeeDay(employeeId, dateVote);
        if ((voteItem != null) && (voteItem.getId() > 0)) {
            throw new ValidationException("Profissional já votou nesse dia!");
        }
    }

    private void voteWinRestaurantIdWeek(Long restaurantId, LocalDate dateVote) {
        var dateVoteIni = dateVote.minusDays(dateVote.getDayOfWeek().getValue());
        var dateVoteEnd = dateVoteIni.plusDays(6);
        List<Vote> list = voteRepository.findByWinRestaurantIdWeek(restaurantId, dateVoteIni, dateVoteEnd);
        if (!list.isEmpty()) {
            throw new ValidationException("Restaurante já foi escolhido na semana!");
        }
    }

    public void delete(Long id) {
        try {
            var voteItemResponse = findById(id);
            var vote = voteRepository.findById(voteItemResponse.getVote().getId()).get();
            voteClosed(vote.getStatus());
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Voto não encontrada ID: " + id + " (Err. Vote Service: 03)");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("(Err. Vote Service: 04) " + e.getMessage());
        } catch (ValidationException e) {
            throw new ValidationException("Votação encerrada!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
