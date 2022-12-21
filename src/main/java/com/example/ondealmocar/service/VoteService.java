package com.example.ondealmocar.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.ondealmocar.dto.VoteDTO;
import com.example.ondealmocar.dto.VoteWinWeek;
import com.example.ondealmocar.dto.projection.IVoteWin;
import com.example.ondealmocar.exception.DatabaseException;
import com.example.ondealmocar.exception.ResourceNotFoundException;
import com.example.ondealmocar.exception.ValidationException;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.enums.VoteStatus;
import com.example.ondealmocar.repository.VoteItemRepository;
import com.example.ondealmocar.repository.VoteRepository;

@Service
public class VoteService {

	@Autowired
	private VoteRepository repository;

	@Autowired
	private VoteItemRepository voteItemRepository;

	public VoteDTO findById(Long id) {
		var vote = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Voto não encontrado Id: " + id + " (Err. Vote Service: 01)"));
		return new VoteDTO(vote);
	}

	public List<VoteDTO> findAll() {
		List<VoteDTO> list = repository.findAll().stream().map(x -> new VoteDTO(x)).collect(Collectors.toList());
		return list;
	}

	public VoteDTO save(VoteDTO request) {
		voteDay(request.getDateVote());
		var vote = repository.save(Vote.of(request));
		return VoteDTO.of(vote);
	}

	private void voteDay(LocalDate dateVote) {
		List<Vote> list = repository.findByDateVote(dateVote);
		if (!list.isEmpty()) {
			throw new ValidationException("Já existe votação cadastrada para esse dia");
		}
	}

	public VoteDTO finish(Long id) {
		try {
			VoteDTO voteDTO = findById(id);
			Vote vote = new Vote(voteDTO);
			var voteItem = voteItemRepository.findByWinDay(vote.getDateVote(), VoteStatus.OPEN);
			validateVotes(voteItem);
			vote.setStatus(VoteStatus.CLOSE);
			vote.setRestaurantWin(voteItem.get(0).getRestaurant());
			return VoteDTO.of(repository.save(vote));
		} catch (RuntimeException e) {
			throw new ResourceNotFoundException("(Err. Vote Service: 03) " + e.getMessage());
		}
	}

	private void validateVotes(List<IVoteWin> voteItem) {
		if (voteItem.size() == 0) {
			throw new ValidationException("Ninguem votou, você não pode finalizar");
		}
	}
	
	public List<VoteWinWeek> findByWinWeek(String dateVoteString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateVote = LocalDate.parse(dateVoteString, formatter);		
		var dateVoteIni = dateVote.minusDays(dateVote.getDayOfWeek().getValue());
		var dateVoteEnd = dateVoteIni.plusDays(7);
		List<VoteWinWeek> list = repository.findByWinRestaurantWeek(dateVoteIni, dateVoteEnd, VoteStatus.CLOSE).stream()
				.map(x -> new VoteWinWeek(x))
				.collect(Collectors.toList());			
		return list;
	}
	
	public List<Vote> findByWinRestaurantIdWeek(Long restaurantId, LocalDate dateVoteIni, LocalDate dateVoteEnd) {	
		List<Vote> list = repository.findByWinRestaurantIdWeek(restaurantId, dateVoteIni, dateVoteEnd);			
		return list;
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Voto não encontrada ID: " + id + " (Err. Vote Service: 03)");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("(Err. Vote Service: 04) " + e.getMessage());
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
