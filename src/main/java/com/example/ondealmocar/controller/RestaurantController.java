package com.example.ondealmocar.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.ondealmocar.dto.RestaurantDTO;
import com.example.ondealmocar.service.RestaurantService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

	@Autowired
	private RestaurantService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<RestaurantDTO> findById(@PathVariable Long id) {
		RestaurantDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping
	public ResponseEntity<List<RestaurantDTO>> findAll() {
		List<RestaurantDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@PostMapping
	public ResponseEntity<RestaurantDTO> save(@RequestBody RestaurantDTO request) {
		var restaurant = service.save(request);
		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").buildAndExpand(restaurant.getId())
				.toUri();
		return ResponseEntity.created(uri).body(restaurant);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<RestaurantDTO> update(@PathVariable Long id, @RequestBody RestaurantDTO obj) {
		findById(id);		
		var restaurant = service.update(id, obj);
		return ResponseEntity.ok().body(restaurant);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
