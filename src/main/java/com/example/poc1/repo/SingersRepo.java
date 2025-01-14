package com.example.poc1.repo;
import com.example.poc1.entity.Singers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingersRepo extends JpaRepository<Singers, Integer> {


}
