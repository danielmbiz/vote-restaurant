package com.example.ondealmocar.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.ondealmocar.dto.VoteItemRequest;
import com.example.ondealmocar.dto.VoteItemResponse;
import com.example.ondealmocar.dto.VoteItemWin;
import com.example.ondealmocar.model.enums.VoteStatus;
import com.example.ondealmocar.service.VoteItemService;

@Controller
@RequestMapping("/votes/vote")
public class VoteItemController {

	@Autowired
	private VoteItemService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<VoteItemResponse> findById(@PathVariable Long id) {
		Optional<VoteItemResponse> obj = Optional.ofNullable(service.findById(id));
		if (obj.isPresent()) {
			return ResponseEntity.ok().body(obj.get());
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping(value = "/win/{dateVote}")
	public ResponseEntity<VoteItemWin> findByWinDay(@PathVariable String dateVote) {
		VoteItemWin obj = service.findByWinDay(dateVote, VoteStatus.CLOSE);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping
	public ResponseEntity<List<VoteItemResponse>> findAll() {
		List<VoteItemResponse> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@PostMapping
	public ResponseEntity<VoteItemResponse> save(@RequestBody VoteItemRequest request) {
		Optional<VoteItemResponse> vote = Optional.ofNullable(service.save(request));
		if (vote.isPresent()) {
			URI uri = ServletUriComponentsBuilder
					.fromCurrentContextPath().path("/{id}")
					.buildAndExpand(vote.get().getId())
					.toUri();
			return ResponseEntity.created(uri).body(vote.get());
		}
		else {
			return ResponseEntity.unprocessableEntity().build();
		}
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
