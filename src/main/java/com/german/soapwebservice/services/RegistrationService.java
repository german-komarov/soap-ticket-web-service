package com.german.soapwebservice.services;


import com.german.soapwebservice.entiites.User;
import com.german.soapwebservice.exceptions.RegistrationException;
import localhost._8080.tickets.CreateUserDto;
import localhost._8080.tickets.CreateUserRequest;
import localhost._8080.tickets.CreateUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Collections;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegistrationService {

    private final UserService userService;


    @Autowired
    public RegistrationService(UserService userService) {
        this.userService = userService;
    }

    public CreateUserResponse createUser(CreateUserRequest createUserRequest) throws RegistrationException {

        CreateUserDto createUserDto = createUserRequest.getUser();

        String username = createUserDto.getUsername();

        if (username.length() < 3 || username.length() > 80) {
            throw new RegistrationException("Username's length must in the range [3,80]");
        }

        String password = createUserDto.getPassword();

        if (!password.equals(createUserDto.getConfirmPassword())) {
            throw new RegistrationException("Password and confirmation password are not matched");
        }


        User user = new User();

        user.setUsername(username);
        user.setPassword(Base64.getEncoder().encodeToString(password.getBytes()));
        user.setRoles(Collections.singleton(User.Role.CLIENT));
        user.setBalance(0);


        this.userService.save(user);


        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setStatus(HttpStatus.CREATED.name());

        return createUserResponse;


    }
}
