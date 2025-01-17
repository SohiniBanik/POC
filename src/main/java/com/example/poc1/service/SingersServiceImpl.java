package com.example.poc1.service;

import com.example.poc1.entity.Singers;
import com.example.poc1.repo.SingersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SingersServiceImpl implements SingersService{

    @Autowired
    private SingersRepo singersRepo;

    @Override
    @Transactional
    public Integer saveSingers(Singers singers) {
        return singersRepo.save(singers).getSingerPosition();
    }

    @Override
    @Transactional
    public void updateSingers(Singers singers) {
        singersRepo.save(singers);
    }

    @Override
    @Transactional
    public void deleteSingers(Integer id) {
        singersRepo.deleteById(id);
    }

    @Override
    @Transactional
    public Singers getSingers(Integer id) {
        return singersRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public boolean isAvailable(Integer id) {
        return singersRepo.existsById(id);
    }

    @Override
    public List<Singers> getAllSingers() {
        return singersRepo.findAll();
    }
}
