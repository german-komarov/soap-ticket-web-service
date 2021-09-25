package com.german.soapwebservice.enpoints;


import com.german.soapwebservice.exceptions.RegistrationException;
import com.german.soapwebservice.exceptions.RetrieveClientInformationException;
import com.german.soapwebservice.exceptions.TopUpBalanceException;
import com.german.soapwebservice.services.RegistrationService;
import com.german.soapwebservice.services.UserService;
import localhost._8080.tickets.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static com.german.soapwebservice.util.Constants.NAMESPACE_URI;

@Endpoint
public class UserEndpoint {


    private final UserService userService;
    private final RegistrationService registrationService;


    @Autowired
    public UserEndpoint(UserService userService, RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createUserRequest")
    @ResponsePayload
    private CreateUserResponse createUser(@RequestPayload CreateUserRequest createUserRequest) throws RegistrationException {

        CreateUserResponse createUserResponse = this.registrationService.createUser(createUserRequest);

        return createUserResponse;

    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "retrieveClientInformationRequest")
    @ResponsePayload
    public RetrieveClientInformationResponse retrieveClientInformation(@RequestPayload RetrieveClientInformationRequest retrieveClientInformationRequest) throws RetrieveClientInformationException {

        RetrieveClientInformationResponse retrieveClientInformationResponse = this.userService.retrieveClientInformation(retrieveClientInformationRequest);


        return retrieveClientInformationResponse;

    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "topUpBalanceRequest")
    @ResponsePayload
    public TopUpBalanceResponse topUpBalance(@RequestPayload TopUpBalanceRequest topUpBalanceRequest) throws TopUpBalanceException {

        TopUpBalanceResponse topUpBalanceResponse = this.userService.topUpBalance(topUpBalanceRequest);


        return topUpBalanceResponse;


    }


}
