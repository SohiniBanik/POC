package com.example.poc1.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingersTest {

    @Test
    public void testSingersGettersAndSetters() {
        Singers singer = new Singers();

        singer.setSingerPosition(1);
        singer.setSingerName("John Doe");
        singer.setSingersRenumeration(50000.0);

        assertEquals(1, singer.getSingerPosition());
        assertEquals("John Doe", singer.getSingerName());
        assertEquals(50000.0, singer.getSingersRenumeration());
    }

    @Test
    public void testSingersConstructor() {
        Singers singer = new Singers(1, "Jane Doe", 60000.0);

        assertEquals(1, singer.getSingerPosition());
        assertEquals("Jane Doe", singer.getSingerName());
        assertEquals(60000.0, singer.getSingersRenumeration());
    }

    @Test
    public void testSingersNoArgsConstructor() {

        Singers singer = new Singers();

        assertEquals(null, singer.getSingerPosition());
        assertEquals(null, singer.getSingerName());
        assertEquals(null, singer.getSingersRenumeration());
    }
}