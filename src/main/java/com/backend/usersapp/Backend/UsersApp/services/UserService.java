package com.backend.usersapp.Backend.UsersApp.services;

import com.backend.usersapp.Backend.UsersApp.models.entities.User;
import com.backend.usersapp.Backend.UsersApp.models.request.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void remove(Long id);
    Optional<User> update(UserRequest user, Long id);
}
