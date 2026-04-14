package com.example.RedisImplimentation.services;

import com.example.RedisImplimentation.models.User;
import com.example.RedisImplimentation.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepo userRepo;
    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String,List<User>> listRedisTemplate;

    @Autowired
    public UserService(UserRepo userRepo,
                       RedisTemplate<String, User> userRedisTemplate,RedisTemplate<String,List<User>> listRedisTemplate) {
        this.userRepo = userRepo;
        this.userRedisTemplate = userRedisTemplate;
        this.listRedisTemplate = listRedisTemplate;
    }

    public List<User> getUsers() {
        String key = "users";

        List<User> redisUser = listRedisTemplate.opsForValue().get(key);

        if(redisUser != null){
            return redisUser;
        }
        log.info("Fetching all users from database");
        List<User> users = userRepo.findAll();
        log.debug("Retrieved {} users", users.size());
        listRedisTemplate.opsForValue().set(key,users);
        return users;
    }

    public Optional<User> getUserById(int id) {
        String key = "user:" + id;

        User user = userRedisTemplate.opsForValue().get(key);

        if (user != null) {
            log.info("Cache HIT ", id);
            return Optional.of(user);
        }

        log.info("Cache MISS ", id);

        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            userRedisTemplate.opsForValue().set(key, u);
            log.info("User cached in REDIS", id);
        }

        return optionalUser;
    }

    public User addUser(User u) {
        log.info("Adding new user: {}", u.getId());
        User savedUser = userRepo.save(u);
        String userKey = "user:" + savedUser.getId();

        userRedisTemplate.opsForValue().set(userKey, savedUser, 5, TimeUnit.MINUTES);

        log.info("User {} saved and cached", savedUser.getId());

        return savedUser;
    }
}