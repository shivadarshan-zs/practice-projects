package com.example.RedisImplimentation.repositories;

import com.example.RedisImplimentation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
