package com.example.ondealmocar.service;

import com.example.ondealmocar.dto.RestaurantDTO;
import com.example.ondealmocar.exception.DatabaseException;
import com.example.ondealmocar.exception.ResourceNotFoundException;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {
    public static final Restaurant RESTAURANT = new Restaurant(null, "nome");
    public static final Restaurant INVALID_RESTAURANT = new Restaurant(null, "");
    @InjectMocks
    private RestaurantService service;
    @Mock
    private RestaurantRepository repository;

    @Test
    public void findByIdRestaurant_WithValidData_ReturnsRestaurant() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(RESTAURANT));
        var dto = RestaurantDTO.of(RESTAURANT);
        RestaurantDTO sut = service.findById(1L);

        assertEquals(RestaurantDTO.class, sut.getClass());
        assertNotNull(sut);
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getName(), sut.getName());
    }

    @Test
    public void findByIdRestaurant_WithInvalidData_ReturnsResourceNotFound() {
        when(repository.findById(anyLong())).thenThrow(new ResourceNotFoundException(""));

        try {
            service.findById(999L);
        } catch (ResourceNotFoundException e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
        }
    }

    @Test
    public void findByAllRestaurant_WithValidData_ReturnsListRestaurant() {
        List<Restaurant> list = new ArrayList<>();
        list.add(RESTAURANT);
        RestaurantDTO dto = new RestaurantDTO(RESTAURANT);
        when(repository.findAll()).thenReturn(list);

        List<RestaurantDTO> sut = service.findAll();

        assertThat(sut).asList().isNotEmpty();
        assertThat(sut).asList().hasSize(1);
        assertEquals(RestaurantDTO.class, sut.get(0).getClass());
        assertEquals(dto.getId(), sut.get(0).getId());
        assertEquals(dto.getName(), sut.get(0).getName());
    }

    @Test
    public void createRestaurant_WithValidData_ReturnsRestaurant() {
        when(repository.save(any())).thenReturn(RESTAURANT);
        var dto = RestaurantDTO.of(RESTAURANT);

        RestaurantDTO sut = service.save(dto);

        assertNotNull(sut);
        assertEquals(RestaurantDTO.class, sut.getClass());
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getName(), sut.getName());
    }

    @Test
    public void createRestaurant_DataIntegraty_RuntimeException() {
        when(repository.save(any())).thenThrow(RuntimeException.class);
        var dto = RestaurantDTO.of(INVALID_RESTAURANT);

        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createRestaurant_DataIntegraty_DataIntegrityViolationReturnDatabaseException() {
        when(repository.save(any())).thenThrow(DataIntegrityViolationException.class);
        var dto = RestaurantDTO.of(INVALID_RESTAURANT);

        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(DatabaseException.class);
    }

    @Test
    public void createRestaurant_EmailDataIntegraty_DataIntegrityViolationException() {
        when(repository.save(any())).thenReturn(RESTAURANT);

        try {
            RESTAURANT.setId(1L);
            var dto = RestaurantDTO.of(RESTAURANT);
            service.save(dto);
        } catch (DataIntegrityViolationException e) {
            assertEquals(DatabaseException.class, e.getClass());
        }
    }

    @Test
    public void updateRestaurant_WithValidData_ReturnsRestaurant() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(RESTAURANT));
        when(repository.save(any())).thenReturn(RESTAURANT);

        var dto = RestaurantDTO.of(RESTAURANT);

        RestaurantDTO sut = service.update(1L, dto);
        assertNotNull(sut);
        assertEquals(RestaurantDTO.class, sut.getClass());
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getName(), sut.getName());
    }

    @Test
    public void updateRestaurant_DataIntegraty_RuntimeException() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(RESTAURANT));
        when(repository.save(any())).thenThrow(RuntimeException.class);
        var dto = RestaurantDTO.of(RESTAURANT);

        assertThatThrownBy(() -> service.update(1L, dto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void deleteRestaurant_WithValidData_doesNotThrowsException() {
        assertThatCode(() -> service.delete(anyLong())).doesNotThrowAnyException();
    }

    @Test
    public void deleteRestaurant_DataIntegraty_DatabaseException() {
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());
        assertThatThrownBy(() -> service.delete(anyLong())).isInstanceOf(DatabaseException.class);
    }

    @Test
    public void deleteRestaurant_DataIntegraty_EmptyResultDataAccessException() {
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());
        assertThatThrownBy(() -> service.delete(anyLong())).isInstanceOf(ResourceNotFoundException.class);
    }

}
