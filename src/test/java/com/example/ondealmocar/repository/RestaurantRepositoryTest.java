package com.example.ondealmocar.repository;

import com.example.ondealmocar.model.Employee;
import com.example.ondealmocar.model.Restaurant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    TestEntityManager entity;

    public final static Restaurant RESTAURANT = new Restaurant(null, "nome");
    public final static Restaurant INVALID_RESTAURANT = new Restaurant(null, "");
    public final static Restaurant EMPTY_RESTAURANT = new Restaurant();

    @AfterEach
    public void afterEach() {
        RESTAURANT.setId(null);
    }

    @Test
    public void createRestaurant_WithValidData_ReturnsRestaurant() {
        Restaurant restaurant = repository.save(RESTAURANT);
        Restaurant sut = entity.find(Restaurant.class, restaurant.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(RESTAURANT.getName());
        assertThat(sut.getName()).isEqualTo(RESTAURANT.getName());
    }

    @Test
    public void createRestaurant_WithInvalidData_ThrowsException() {
        assertThatThrownBy(() -> repository.save(INVALID_RESTAURANT)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> repository.save(EMPTY_RESTAURANT)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findByIdRestaurant_ByExistingId_ReturnsRestaurant() {
        Restaurant restaurant = entity.persistFlushFind(RESTAURANT);
        Optional<Restaurant> sut = repository.findById(restaurant.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(restaurant);
    }

    @Test
    public void findByIdRestaurant_ByUnexistingId_ReturnsEmpty() {
        Optional<Restaurant> sut = repository.findById(999L);

        assertThat(sut).isEmpty();
    }

}

