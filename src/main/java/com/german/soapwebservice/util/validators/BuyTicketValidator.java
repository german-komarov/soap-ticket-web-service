package com.german.soapwebservice.util.validators;

import com.german.soapwebservice.entiites.Ticket;
import com.german.soapwebservice.entiites.User;
import com.german.soapwebservice.exceptions.BuyTicketDtoNotValidException;
import com.german.soapwebservice.exceptions.UserCredentialsNotValidException;
import com.german.soapwebservice.services.UserService;
import localhost._8080.tickets.BuyTicketDto;
import localhost._8080.tickets.UserCredentialsDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BuyTicketValidator {


    private final UserService userService;


    public BuyTicketValidator(UserService userService) {
        this.userService = userService;
    }


    public User validateAndReturnUser(BuyTicketDto buyTicketDto, Ticket ticket) throws BuyTicketDtoNotValidException {

        UserCredentialsDto userCredentialsDto = buyTicketDto.getBuyerCredentials();

        User user;


        try {
            user = this.userService.validateAndReturnUser(userCredentialsDto);
        } catch (UserCredentialsNotValidException e) {
            throw new BuyTicketDtoNotValidException(e.getMessage());
        }


        // Amount of ticket that customer wants to buy
        int desiredAmountOfTickets = buyTicketDto.getPlaces().size();


        // Total amount of such tickets
        int totalAmountOfTicket = ticket.getNumberOfPlaces();


        // Amount of ticket that were already bought
        int amountOfBoughtTickets = ticket.getBoughtPlaceNumbers().size();


        // Amount of ticket that is available right now
        int availableAmountOfTickets = totalAmountOfTicket - amountOfBoughtTickets;


        if (desiredAmountOfTickets > availableAmountOfTickets) {
            throw new BuyTicketDtoNotValidException("Sorry, but there is no such amount if these tickets");
        }


        // Places that customer want to buy
        List<Integer> desiredPlaces = buyTicketDto.getPlaces();

        // Places that are already bought
        Set<Integer> boughtPlaces = ticket.getBoughtPlaceNumbers();


        // Places that were desired, but no available because they are bought
        List<Integer> allNotAvailablePlaces = new ArrayList<>();


        Stream<Integer> term = desiredPlaces.stream();

        term = term.filter((placeNumber) -> placeNumber > totalAmountOfTicket || placeNumber <= 0);

        allNotAvailablePlaces = term.collect(Collectors.toList());


        if (!allNotAvailablePlaces.isEmpty()) {
            throw new BuyTicketDtoNotValidException(String.format("Following place(s) : %s are not available as they are either great than maximum possible number, or less than minimum possible number", allNotAvailablePlaces.toString()));
        }


        term = desiredPlaces.stream();

        term = term.filter(boughtPlaces::contains);

        allNotAvailablePlaces = term.collect(Collectors.toList());


        if (!allNotAvailablePlaces.isEmpty()) {

            throw new BuyTicketDtoNotValidException(String.format("Following place(s) : %s are not available as they are already bought", allNotAvailablePlaces.toString()));

        }


        if (user.getBalance() < desiredAmountOfTickets * ticket.getPrice()) {

            throw new BuyTicketDtoNotValidException("You do not have enough money on your balance");

        }


        return user;

    }


}
