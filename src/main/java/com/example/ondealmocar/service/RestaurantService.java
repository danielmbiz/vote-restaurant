package com.example.ondealmocar.service;

import com.example.ondealmocar.dto.RestaurantDTO;
import com.example.ondealmocar.exception.DatabaseException;
import com.example.ondealmocar.exception.ResourceNotFoundException;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    public RestaurantDTO findById(Long id) {
        var restaurant = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Restaurante não encontrado Id: " + id + " (Err. Restaurant Service: 01)"));
        return new RestaurantDTO(restaurant);

    }

    public List<RestaurantDTO> findAll() {
        return repository.findAll().stream().map(RestaurantDTO::of)
                .collect(Collectors.toList());
    }

    public RestaurantDTO save(RestaurantDTO request) {
        try {
            var restaurant = repository.save(Restaurant.of(request));
            return RestaurantDTO.of(restaurant);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("(Err. Restaurant Service: 05) " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Erro não definido");
        }
    }


    public RestaurantDTO update(Long id, RestaurantDTO obj) {
        try {
            findById(id);
            var restaurant = Restaurant.of(obj);
            restaurant.setId(id);
            return RestaurantDTO.of(repository.save(restaurant));
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("(Err. Restaurant Service: 02) " + e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Restaurante não encontrada ID: " + id + " (Err. Restaurant Service: 03)");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("(Err. Restaurant Service: 04) " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
