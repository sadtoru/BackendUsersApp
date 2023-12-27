package com.backend.usersapp.Backend.UsersApp.services;

import com.backend.usersapp.Backend.UsersApp.models.entities.Role;
import com.backend.usersapp.Backend.UsersApp.repositories.RoleRepository;
import com.backend.usersapp.Backend.UsersApp.repositories.UserRepository;
import com.backend.usersapp.Backend.UsersApp.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> optionalUser = roleRepository.findByName("ROLE_USER");

        List<Role> roleList = new ArrayList<>();

        if(optionalUser.isPresent()){
            roleList.add(optionalUser.orElseThrow());
        }
        user.setRoles(roleList);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> update(User user, Long id) {
        Optional<User> o = this.findById(id);
        if (o.isPresent()) {
            User userDb = o.orElseThrow();
            userDb.setUsername(userDb.getUsername());
            userDb.setEmail(userDb.getEmail());
            return Optional.of(this.save(userDb));
        }
        return Optional.empty();
    }


}
