package com.example.ondealmocar.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.ondealmocar.dto.projection.IVoteWinWeek;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.enums.VoteStatus;

public interface VoteRepository extends JpaRepository<Vote, Long> {

	public List<Vote> findByDateVote(LocalDate dateVote);
	
	@Query("SELECT v.dateVote as dateVote, v.restaurantWin as restaurant "
			+ "FROM Vote v "
			+ "WHERE v.dateVote >= :dateVoteIni "
			+ "AND v.dateVote <= :dateVoteEnd "
			+ "AND v.status = :status "
			+ "ORDER BY v.dateVote")
	public List<IVoteWinWeek> findByWinRestaurantWeek(LocalDate dateVoteIni, LocalDate dateVoteEnd, VoteStatus status);
	
	@Query("SELECT v "
			+ "FROM Vote v "
			+ "WHERE v.restaurantWin.id = :restaurantId "
			+ "AND v.dateVote >= :dateVoteIni "
			+ "AND v.dateVote <= :dateVoteEnd "
			+ "ORDER BY v.dateVote DESC")
	public List<Vote> findByWinRestaurantIdWeek(Long restaurantId, LocalDate dateVoteIni, LocalDate dateVoteEnd);

}
