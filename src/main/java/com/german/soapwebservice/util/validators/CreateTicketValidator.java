package com.german.soapwebservice.util.validators;


import com.german.soapwebservice.entiites.User;
import com.german.soapwebservice.exceptions.CreateTicketRequestNotValidException;
import com.german.soapwebservice.exceptions.UserCredentialsNotValidException;
import com.german.soapwebservice.services.UserService;
import localhost._8080.tickets.CreateTicketDto;
import localhost._8080.tickets.CreateTicketRequest;
import localhost._8080.tickets.UserCredentialsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CreateTicketValidator {


    private final UserService userService;


    @Autowired
    public CreateTicketValidator(UserService userService) {
        this.userService = userService;
    }


    public void validate(CreateTicketRequest createTicketRequest) throws CreateTicketRequestNotValidException {

        UserCredentialsDto userCredentialsDto = createTicketRequest.getCreatorCredentials();


        User user;

        try {
            user = this.userService.validateAndReturnUser(userCredentialsDto);
        } catch (UserCredentialsNotValidException e) {
            throw new CreateTicketRequestNotValidException(e.getMessage());
        }


        if (!user.getRoles().contains(User.Role.EMPLOYEE)) {
            throw new CreateTicketRequestNotValidException("You do not have access to this action, as you not employee");
        }


        CreateTicketDto createTicketDto = createTicketRequest.getTicket();


        if (createTicketDto.getPrice() < 0f) {
            throw new CreateTicketRequestNotValidException("Price cannot be negative");
        }

        if (createTicketDto.getNumberOfPlaces() < 0) {
            throw new CreateTicketRequestNotValidException("Number of places cannot be negative");
        }


        LocalDateTime localDateTime;

        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            localDateTime = LocalDateTime.parse(createTicketDto.getDateTime(), dateTimeFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreateTicketRequestNotValidException(e.getMessage());
        }


        if (localDateTime.isBefore(LocalDateTime.now())) {

            throw new CreateTicketRequestNotValidException("Fly date of new ticket cannot be past date");
        }


    }
}
