package com.example.poc1.service;


import com.example.poc1.entity.Singers;

import java.util.List;

public interface SingersService {
    public Integer saveSingers(Singers singers);
    public void updateSingers(Singers singers);
    public void deleteSingers(Integer id);
    public Singers getSingers(Integer id);
    public boolean isAvailable(Integer id);
    public List<Singers> getAllSingers();
}
