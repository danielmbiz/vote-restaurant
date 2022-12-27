package com.example.ondealmocar.controller;

import com.example.ondealmocar.dto.EmployeeDTO;
import com.example.ondealmocar.dto.VoteDTO;
import com.example.ondealmocar.dto.VoteWinWeek;
import com.example.ondealmocar.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<VoteDTO> findById(@PathVariable Long id) {
        Optional<VoteDTO> obj = Optional.ofNullable(service.findById(id));
        if (obj.isPresent()) {
            return ResponseEntity.ok().body(obj.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/vote/win/week/{dateVoteString}")
    public ResponseEntity<List<VoteWinWeek>> findByWinWeek(@PathVariable String dateVoteString) {
        List<VoteWinWeek> list = service.findByWinWeek(dateVoteString);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping
    public ResponseEntity<List<VoteDTO>> findAll() {
        List<VoteDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<VoteDTO> save(@RequestBody VoteDTO request) {
        Optional<VoteDTO> vote = Optional.ofNullable(service.save(request));
        if (vote.isPresent()) {
            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").buildAndExpand(vote.get().getId())
                    .toUri();
            return ResponseEntity.created(uri).body(vote.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
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
