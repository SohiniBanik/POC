package com.example.poc1.service;

import com.example.poc1.entity.Singers;
import com.example.poc1.repo.SingersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SingersServiceImplTest {

    @InjectMocks
    private SingersServiceImpl singersService;

    @Mock
    private SingersRepo singersRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveSingers() {
        Singers singer = new Singers();
        singer.setSingerPosition(1);
        singer.setSingerName("John Doe");
        singer.setSingersRenumeration(50000.0);

        when(singersRepo.save(any(Singers.class))).thenReturn(singer);

        Integer savedId = singersService.saveSingers(singer);

        assertEquals(1, savedId);
        verify(singersRepo, times(1)).save(singer);
    }

    @Test
    public void testUpdateSingers() {
        Singers singer = new Singers();
        singer.setSingerPosition(1);
        singer.setSingerName("John Doe");
        singer.setSingersRenumeration(50000.0);

        singersService.updateSingers(singer);

        verify(singersRepo, times(1)).save(singer);
    }

    @Test
    public void testDeleteSingers() {
        Integer id = 1;

        singersService.deleteSingers(id);

        verify(singersRepo, times(1)).deleteById(id);
    }

    @Test
    public void testGetSingers_Success() {
        Integer id = 1;
        Singers singer = new Singers();
        singer.setSingerPosition(id);
        singer.setSingerName("John Doe");
        singer.setSingersRenumeration(50000.0);

        when(singersRepo.findById(id)).thenReturn(Optional.of(singer));

        Singers fetchedSinger = singersService.getSingers(id);

        assertEquals(singer, fetchedSinger);
    }

    @Test
    public void testGetSingers_NotFound() {
        Integer id = 1;

        when(singersRepo.findById(id)).thenReturn(Optional.empty());

        Singers fetchedSinger = singersService.getSingers(id);

        assertNull(fetchedSinger);
    }

    @Test
    public void testIsAvailable_Exists() {
        Integer id = 1;

        when(singersRepo.existsById(id)).thenReturn(true);

        boolean exists = singersService.isAvailable(id);

        assertEquals(true, exists);
    }

    @Test
    public void testIsAvailable_NotExists() {
        Integer id = 1;

        when(singersRepo.existsById(id)).thenReturn(false);

        boolean exists = singersService.isAvailable(id);

        assertEquals(false, exists);
    }

    @Test
    public void testGetAllSingers() {
        Singers singer1 = new Singers(1, "John Doe", 50000.0);
        Singers singer2 = new Singers(2, "Jane Doe", 60000.0);
        when(singersRepo.findAll()).thenReturn(Arrays.asList(singer1, singer2));

        List<Singers> allSingers = singersService.getAllSingers();

        assertEquals(2, allSingers.size());
        assertEquals("John Doe", allSingers.get(0).getSingerName());
        assertEquals("Jane Doe", allSingers.get(1).getSingerName());
    }
}