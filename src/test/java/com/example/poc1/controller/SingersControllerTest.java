package com.example.poc1.controller;

import com.example.poc1.entity.Singers;
import com.example.poc1.kafka.KafkaProducerService;
import com.example.poc1.service.SingersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SingersControllerTest {

    @InjectMocks
    private SingersController singersController;

    @Mock
    private SingersServiceImpl singersService;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Singers singer = new Singers();
        singer.setSingerPosition(1);

        when(singersService.saveSingers(any(Singers.class))).thenReturn(1);

        ResponseEntity<String> response = singersController.save(singer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Singer1created", response.getBody());
    }

    @Test
    public void testUpdate_Success() {
        Singers singer = new Singers();
        singer.setSingerPosition(1);

        when(singersService.isAvailable(anyInt())).thenReturn(true);

        ResponseEntity<String> response = singersController.update(singer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Successfully", response.getBody());
        verify(singersService, times(1)).updateSingers(singer);
    }

    @Test
    public void testUpdate_NotFound() {
        Singers singer = new Singers();
        singer.setSingerPosition(1);

        when(singersService.isAvailable(anyInt())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            singersController.update(singer);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode()); // Check the status code
        assertEquals("Record 1 not found", exception.getReason()); // Check the exception message
        verify(singersService, never()).updateSingers(any(Singers.class));
    }

    @Test
    public void testDeleteById_Success() {
        Integer id = 1;

        when(singersService.isAvailable(id)).thenReturn(true);

        ResponseEntity<String> response = singersController.deleteById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody());

        verify(singersService, times(1)).deleteSingers(id);
    }

    @Test
    public void testDeleteById_NotFound() {
        int id = 1;

        ResponseEntity<String> response = singersController.deleteById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Singer " + id + "not found", response.getBody());

        verify(singersService, times(1)).isAvailable(id);
        verify(singersService, never()).deleteSingers(anyInt()); // Ensure deleteSingers is not called
        verify(kafkaProducerService, times(1)).sendMessage(anyString(), any());
    }


    @Test
    public void testGetSingersById_Success() {
        int id = 1;
        Singers singer = new Singers();
        singer.setSingerPosition(id);

        when(singersService.isAvailable(id)).thenReturn(true);
        when(singersService.getSingers(id)).thenReturn(singer);

        ResponseEntity<Singers> response = singersController.getSingersById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(singer, response.getBody());
    }

    @Test
    public void testGetSingersById_NotFound() {
        int id = 1;
        when(singersService.isAvailable(id)).thenReturn(false);

        ResponseEntity<Singers> response = singersController.getSingersById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new Singers(), response.getBody());
        verify(singersService, times(1)).isAvailable(id);
        verify(singersService, never()).getSingers(id);
    }
}
