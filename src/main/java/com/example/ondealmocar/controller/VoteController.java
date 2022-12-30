package com.example.ondealmocar.controller;

import com.example.ondealmocar.dto.VoteDTO;
import com.example.ondealmocar.dto.VoteWinWeek;
import com.example.ondealmocar.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<VoteDTO> findById(@PathVariable Long id) {
        var obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/vote/win/week/{dateVoteString}")
    public ResponseEntity<List<VoteWinWeek>> findByWinWeek(@PathVariable String dateVoteString) {
        var list = service.findByWinWeek(dateVoteString);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping
    public ResponseEntity<List<VoteDTO>> findAll() {
        var list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<VoteDTO> save(@RequestBody VoteDTO request) {
        var vote = service.save(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").buildAndExpand(vote.getId())
                .toUri();
        return ResponseEntity.created(uri).body(vote);
    }

    @PutMapping(value = "/finish/{id}")
    public ResponseEntity<VoteDTO> finish(@PathVariable Long id) {
        findById(id);
        var vote = service.finish(id);
        return ResponseEntity.ok().body(vote);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
