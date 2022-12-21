package com.example.ondealmocar.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.ondealmocar.dto.projection.IVoteWin;
import com.example.ondealmocar.model.VoteItem;
import com.example.ondealmocar.model.enums.VoteStatus;

public interface VoteItemRepository extends JpaRepository<VoteItem, Long> {

	@Query("SELECT v FROM VoteItem v "
			+ "WHERE v.employee.id = :employeeId "
			+ "AND v.vote.dateVote = :dateVote ")
	public VoteItem findByEmployeeDay(Long employeeId, LocalDate dateVote);	
	
	@Query("SELECT "
			+ "v.vote.dateVote AS dateVote, "
			+ "count(v.restaurant.id) AS quantityVote, " 
			+ "v.restaurant AS restaurant "
			+ "FROM VoteItem v "
			+ "WHERE v.vote.dateVote = :dateVote "
			+ "AND v.vote.status = :status "
			+ "GROUP BY v.vote.dateVote, v.restaurant "
			+ "ORDER BY quantityVote DESC")
	public List<IVoteWin> findByWinDay(LocalDate dateVote, VoteStatus status);
	
}
