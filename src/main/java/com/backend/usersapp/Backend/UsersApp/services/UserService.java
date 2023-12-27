package com.backend.usersapp.Backend.UsersApp.services;

import com.backend.usersapp.Backend.UsersApp.models.dto.UserDto;
import com.backend.usersapp.Backend.UsersApp.models.entities.User;
import com.backend.usersapp.Backend.UsersApp.models.request.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();
    Optional<UserDto> findById(Long id);
    UserDto save(User user);
    void remove(Long id);
    Optional<UserDto> update(UserRequest user, Long id);
}
