package com.german.soapwebservice.services;

import com.german.soapwebservice.entiites.Ticket;
import com.german.soapwebservice.entiites.User;
import com.german.soapwebservice.exceptions.*;
import com.german.soapwebservice.repositories.TicketRepository;
import com.german.soapwebservice.util.dto.RetrieveTicketQueryDto;
import com.german.soapwebservice.util.parsers.RetrieveTicketRequestParser;
import com.german.soapwebservice.util.parsers.TicketSliceParser;
import com.german.soapwebservice.util.validators.BuyTicketValidator;
import com.german.soapwebservice.util.validators.CreateTicketValidator;
import localhost._8080.tickets.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TicketService {


    private final TicketRepository ticketRepository;
    private final CreateTicketValidator createTicketValidator;
    private final BuyTicketValidator buyTicketValidator;
    private final UserService userService;
    private final RetrieveTicketRequestParser retrieveTicketRequestParser;
    private final TicketSliceParser ticketSliceParser;


    @Autowired
    public TicketService(TicketRepository ticketRepository, CreateTicketValidator createTicketValidator, BuyTicketValidator buyTicketValidator, UserService userService, RetrieveTicketRequestParser retrieveTicketRequestParser, TicketSliceParser ticketSliceParser) {
        this.ticketRepository = ticketRepository;
        this.createTicketValidator = createTicketValidator;
        this.buyTicketValidator = buyTicketValidator;
        this.userService = userService;
        this.retrieveTicketRequestParser = retrieveTicketRequestParser;
        this.ticketSliceParser = ticketSliceParser;
    }


    public Ticket retrieveById(Long id) throws TicketNotFoundException {

        Ticket ticket =
                this.ticketRepository
                        .findById(id)
                        .orElseThrow(() -> new TicketNotFoundException("There is no ticket with such id"));

        return ticket;
    }


    public Ticket save(Ticket ticket) {

        ticket = this.ticketRepository.save(ticket);

        return ticket;

    }


    public RetrieveTicketResponse retrieveTickets(RetrieveTicketRequest retrieveTicketRequest) throws RetrieveTicketException {

        RequestTicketDto requestTicketDto = retrieveTicketRequest.getTicketCriteria();

        RetrieveTicketQueryDto queryDto;


        try {
            queryDto = this.retrieveTicketRequestParser.parse(requestTicketDto);
        } catch (ParsingException e) {
            throw new RetrieveTicketException(e.getMessage());
        }


        String from = queryDto.getFrom();
        String to = queryDto.getTo();
        String airline = queryDto.getAirline();
        float maxPrice = queryDto.getMaximumPrice();
        List<SalonClass> salonClasses = queryDto.getSalonClasses();
        LocalDateTime minDateTime = queryDto.getFromDateTime();
        LocalDateTime maxDateTime = queryDto.getToDateTime();
        Pageable pageable = queryDto.getPageable();


        Slice<Ticket> tickets = this.ticketRepository.findTicketsByAllAttributes(from, to, airline, maxPrice, salonClasses, minDateTime, maxDateTime, pageable);


        RetrieveTicketResponse retrieveTicketResponse = this.ticketSliceParser.parse(tickets);


        return retrieveTicketResponse;
    }


    public CreateTicketResponse createTicket(CreateTicketRequest createTicketRequest) throws CreateTicketException {


        try {

            this.createTicketValidator.validate(createTicketRequest);

        } catch (CreateTicketRequestNotValidException e) {
            throw new CreateTicketException(e.getMessage());
        }


        Ticket ticket = Ticket.of(createTicketRequest.getTicket());

        this.save(ticket);


        CreateTicketResponse createTicketResponse = new CreateTicketResponse();

        createTicketResponse.setStatus(HttpStatus.CREATED.name());

        return createTicketResponse;


    }

    public BuyTicketResponse buyTicket(BuyTicketRequest buyTicketRequest) throws BuyTicketException {

        BuyTicketDto buyTicketDto = buyTicketRequest.getTicket();


        Ticket ticket;

        try {
            ticket = this.retrieveById(buyTicketDto.getTicketId());
        } catch (TicketNotFoundException e) {
            throw new BuyTicketException(e.getMessage());
        }


        User user;


        try {
            user = this.buyTicketValidator.validateAndReturnUser(buyTicketDto, ticket);
        } catch (BuyTicketDtoNotValidException e) {
            throw new BuyTicketException(e.getMessage());
        }


        buyTicketDto.getPlaces().forEach(ticket.getBoughtPlaceNumbers()::add);

        this.save(ticket);


        buyTicketDto.getPlaces().forEach((placeNumber) -> user.getBoughtTickets().put(placeNumber, ticket.getId()));

        float spentMoney = buyTicketDto.getPlaces().size() * ticket.getPrice();
        float initialUserBalance = user.getBalance();
        float finalUserBalance = initialUserBalance - spentMoney;

        user.setBalance(finalUserBalance);


        this.userService.save(user);


        BuyTicketResponse buyTicketResponse = new BuyTicketResponse();

        buyTicketResponse.setMessage("Your tickets purchase success, check you client cabinet to see all your bought tickets");


        return buyTicketResponse;


    }


}
