package com.backend.usersapp.Backend.UsersApp.services;

import com.backend.usersapp.Backend.UsersApp.models.dto.UserDto;
import com.backend.usersapp.Backend.UsersApp.models.dto.mapper.DtoMapperUser;
import com.backend.usersapp.Backend.UsersApp.models.entities.Role;
import com.backend.usersapp.Backend.UsersApp.models.request.UserRequest;
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
import java.util.stream.Collectors;

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
    public List<UserDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users
                .stream()
                .map(user -> DtoMapperUser.builder().setUser(user).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> DtoMapperUser.builder().setUser(user).build());
    }

    @Override
    @Transactional
    public UserDto save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> optionalUser = roleRepository.findByName("ROLE_USER");

        List<Role> roleList = new ArrayList<>();

        if(optionalUser.isPresent()){
            roleList.add(optionalUser.orElseThrow());
        }
        user.setRoles(roleList);

        return DtoMapperUser.builder().setUser(userRepository.save(user)).build();
    }

    @Override
    @Transactional
    public Optional<UserDto> update(UserRequest user, Long id) {
        Optional<User> o = userRepository.findById(id);
        User userOptional = null;
        if (o.isPresent()) {
            User userDb = o.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = userRepository.save(userDb);
        }
        return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
    }

    @Override
    @Transactional
    public void remove(Long id) {
        userRepository.deleteById(id);
    }


}
