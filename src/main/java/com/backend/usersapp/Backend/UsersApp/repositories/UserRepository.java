package com.backend.usersapp.Backend.UsersApp.repositories;

import com.backend.usersapp.Backend.UsersApp.models.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByUsername(String username);
}
