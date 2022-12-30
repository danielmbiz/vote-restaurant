package com.example.ondealmocar.controller;

import com.example.ondealmocar.dto.VoteItemRequest;
import com.example.ondealmocar.dto.VoteItemResponse;
import com.example.ondealmocar.dto.VoteItemWin;
import com.example.ondealmocar.model.enums.VoteStatus;
import com.example.ondealmocar.service.VoteItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/votes/vote")
public class VoteItemController {

    @Autowired
    private VoteItemService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<VoteItemResponse> findById(@PathVariable Long id) {
        var obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/win/{dateVote}")
    public ResponseEntity<VoteItemWin> findByWinDay(@PathVariable String dateVote) {
        var obj = service.findByWinDay(dateVote, VoteStatus.CLOSE);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<VoteItemResponse>> findAll() {
        var list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<VoteItemResponse> save(@RequestBody VoteItemRequest request) {
        var vote = service.save(request);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/{id}")
                .buildAndExpand(vote.getId())
                .toUri();
        return ResponseEntity.created(uri).body(vote);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
