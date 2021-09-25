package com.german.soapwebservice.enpoints;


import com.german.soapwebservice.exceptions.BuyTicketException;
import com.german.soapwebservice.exceptions.CreateTicketException;
import com.german.soapwebservice.exceptions.RetrieveTicketException;
import com.german.soapwebservice.services.TicketService;
import localhost._8080.tickets.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static com.german.soapwebservice.util.Constants.NAMESPACE_URI;

@Endpoint
public class TicketEndpoint {

    private final TicketService ticketService;


    @Autowired
    public TicketEndpoint(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "retrieveTicketRequest")
    @ResponsePayload
    public RetrieveTicketResponse retrieveTickets(@RequestPayload RetrieveTicketRequest retrieveTicketRequest) throws RetrieveTicketException {

        RetrieveTicketResponse jaxbElement = this.ticketService.retrieveTickets(retrieveTicketRequest);

        return jaxbElement;


    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createTicketRequest")
    @ResponsePayload
    public CreateTicketResponse createTicket(@RequestPayload CreateTicketRequest createTicketRequest) throws CreateTicketException {

        CreateTicketResponse createTicketResponse = this.ticketService.createTicket(createTicketRequest);


        return createTicketResponse;

    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "buyTicketRequest")
    @ResponsePayload
    public BuyTicketResponse buyTicket(@RequestPayload BuyTicketRequest buyTicketRequest) throws BuyTicketException {

        BuyTicketResponse buyTicketResponse = this.ticketService.buyTicket(buyTicketRequest);

        return buyTicketResponse;

    }


}
