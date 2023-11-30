package com.example.taskFlow.service;

import com.example.taskFlow.dto.AuthenticationRequest;
import com.example.taskFlow.dto.AuthenticationResponse;
import com.example.taskFlow.dto.UserDto;
import com.example.taskFlow.jwtConfig.JwtService;
import com.example.taskFlow.model.User;
import com.example.taskFlow.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public UserService(PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authManager) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    //implementing jwtSecurity
    public AuthenticationResponse registerUser(UserDto userDto){
        if(userRepository.existsByEmail(userDto.getUserEmail())){
            throw new KeyAlreadyExistsException("User \'"+ userDto.getUserEmail()+"\' is already related with an instance of User.Class");
        }
        User user = new User(userDto.getUserName(),
                userDto.getUserLastName(),
                userDto.getUserEmail(),
                passwordEncoder.encode(userDto.getUserPassword()) );

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest authRequest){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()
        ));
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(()->new IllegalArgumentException("User "+ authRequest.getEmail()+" isn't related with any instance of User.class"));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    //regular methods
    public User createUser(UserDto userDto){
        if(userRepository.existsByEmail(userDto.getUserEmail())){
            throw new KeyAlreadyExistsException("User \'"+ userDto.getUserEmail()+"\' is already related with an instance of User.Class");
        }
        return userRepository.save(new User(userDto.getUserName(),
                                     userDto.getUserLastName(),
                                     userDto.getUserEmail(),
                                     userDto.getUserPassword()));
    }
    public UserDto getUser(String userEmail){

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Email \'"+ userEmail +"\' isn't related to any instance of User.class"));
        return new UserDto(
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword()
        );

    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(String userEmail, UserDto userDto){
        User user = userRepository.findByEmail(userEmail)
                .map(u -> {u.setEmail(userDto.getUserEmail());
                              u.setPassword(userDto.getUserPassword());
                              u.setName(userDto.getUserName());
                              u.setLastName(userDto.getUserLastName());
                              return u;})
                .orElseThrow(() ->
                        new IllegalArgumentException("Email \'"+ userEmail +"\' isn't related to any instance of User.class"));
        return userRepository.save(user);
    }
    public Boolean deleteUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new IllegalArgumentException("Email \'"+ email +"\' isn't related to any instance of User.class"));

        userRepository.deleteById(user.getId());

        return !userRepository.existsByEmail(email);
    }
}
