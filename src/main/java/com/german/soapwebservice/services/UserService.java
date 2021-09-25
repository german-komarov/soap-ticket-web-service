package com.german.soapwebservice.services;


import com.german.soapwebservice.entiites.User;
import com.german.soapwebservice.exceptions.*;
import com.german.soapwebservice.repositories.UserRepository;
import com.german.soapwebservice.util.parsers.UserParser;
import com.german.soapwebservice.util.validators.BankCardValidator;
import localhost._8080.tickets.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private final UserRepository userRepository;
    private final BankCardValidator bankCardValidator;
    private final UserParser userParser;


    @Autowired
    public UserService(UserRepository userRepository, BankCardValidator bankCardValidator, @Lazy UserParser userParser) {
        this.userRepository = userRepository;
        this.bankCardValidator = bankCardValidator;
        this.userParser = userParser;
    }


    public User retrieveByUsername(String username) throws UserNotFoundException {

        User user =
                this.userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException("There is no user with such username"));


        return user;

    }


    public User save(User user) {

        user = this.userRepository.save(user);

        return user;

    }


    public TopUpBalanceResponse topUpBalance(TopUpBalanceRequest topUpBalanceRequest) throws TopUpBalanceException {

        TopUpBalanceDto topUpBalanceDto = topUpBalanceRequest.getTopUpBalanceData();

        User user;


        try {
            user = this.retrieveByUsername(topUpBalanceDto.getUsername());
        } catch (UserNotFoundException e) {
            throw new TopUpBalanceException(e.getMessage());
        }


        try {

            this.bankCardValidator.validate(topUpBalanceDto.getBankCardCredentials());
        } catch (BankCardCredentialsNotValidException e) {
            throw new TopUpBalanceException(e.getMessage());
        }


        if (topUpBalanceDto.getMoneyAmount() <= 0f) {
            throw new TopUpBalanceException("You cannot put 0 or less dollars to the your balance");
        }


        user.setBalance(user.getBalance() + topUpBalanceDto.getMoneyAmount());

        this.save(user);


        TopUpBalanceResponse topUpBalanceResponse = new TopUpBalanceResponse();
        topUpBalanceResponse.setStatus(HttpStatus.OK.name());

        return topUpBalanceResponse;


    }

    public RetrieveClientInformationResponse retrieveClientInformation(RetrieveClientInformationRequest retrieveClientInformationRequest) throws RetrieveClientInformationException {

        UserCredentialsDto userCredentialsDto = retrieveClientInformationRequest.getClientCredentials();

        User user;


        try {
            user = this.validateAndReturnUser(userCredentialsDto);
        } catch (UserCredentialsNotValidException e) {
            throw new RetrieveClientInformationException(e.getMessage());
        }


        UserInformationDto userInformationDto;

        try {
            userInformationDto = this.userParser.parse(user);
        } catch (ParsingException e) {
            throw new RetrieveClientInformationException(e.getMessage());
        }


        RetrieveClientInformationResponse retrieveClientInformationResponse = new RetrieveClientInformationResponse();

        retrieveClientInformationResponse.setClientInformation(userInformationDto);


        return retrieveClientInformationResponse;
    }


    public User validateAndReturnUser(UserCredentialsDto userCredentialsDto) throws UserCredentialsNotValidException {

        String username = userCredentialsDto.getUsername();

        User user;

        try {
            user = this.retrieveByUsername(username);
        } catch (UserNotFoundException e) {
            throw new UserCredentialsNotValidException(e.getMessage());
        }


        String rawInputPassword = userCredentialsDto.getPassword();

        String base64EncodedInputPassword = Base64.getEncoder().encodeToString(rawInputPassword.getBytes());


        if (!user.getPassword().equals(base64EncodedInputPassword)) {
            throw new UserCredentialsNotValidException("Wrong password");
        }


        return user;

    }


}
