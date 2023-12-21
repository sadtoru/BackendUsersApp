package com.backend.usersapp.Backend.UsersApp;

import com.backend.usersapp.Backend.UsersApp.models.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
