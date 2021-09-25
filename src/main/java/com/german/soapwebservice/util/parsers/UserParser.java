package com.german.soapwebservice.util.parsers;

import com.german.soapwebservice.entiites.Ticket;
import com.german.soapwebservice.entiites.User;
import com.german.soapwebservice.exceptions.ParsingException;
import com.german.soapwebservice.exceptions.TicketNotFoundException;
import com.german.soapwebservice.services.TicketService;
import localhost._8080.tickets.BoughtTicketDto;
import localhost._8080.tickets.UserInformationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserParser {


    private final TicketService ticketService;


    @Autowired
    public UserParser(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    public UserInformationDto parse(User user) throws ParsingException {

        UserInformationDto userInformationDto = new UserInformationDto();

        userInformationDto.setUsername(user.getUsername());
        userInformationDto.setBalance(user.getBalance());

        List<BoughtTicketDto> boughtTicketDtoList;
        try {
            boughtTicketDtoList = this.convertToBoughtTicketDtoList(user.getBoughtTickets());
        } catch (Exception e) {
            throw new ParsingException(e.getMessage());
        }


        userInformationDto.getBoughtTicket().addAll(boughtTicketDtoList);


        return userInformationDto;

    }


    private List<BoughtTicketDto> convertToBoughtTicketDtoList(Map<Integer, Long> boughtTickets) throws Exception {

        List<BoughtTicketDto> boughtTicketDtoList = new ArrayList<>();


        for (Map.Entry<Integer, Long> entry : boughtTickets.entrySet()) {

            Ticket ticket;

            try {
                ticket = this.ticketService.retrieveById(entry.getValue());
            } catch (TicketNotFoundException e) {
                e.printStackTrace();
                throw new Exception("Something went wrong in the server");
            }

            BoughtTicketDto boughtTicketDto = new BoughtTicketDto();

            boughtTicketDto.setId(ticket.getId());
            boughtTicketDto.setFrom(ticket.getFlyFrom());
            boughtTicketDto.setTo(ticket.getFlyTo());
            boughtTicketDto.setAirline(ticket.getAirline());
            boughtTicketDto.setSalonClass(ticket.getSalonClass());
            boughtTicketDto.setDateTime(ticket.getFlyDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            boughtTicketDto.setPlace(entry.getKey());

            boughtTicketDtoList.add(boughtTicketDto);


        }


        return boughtTicketDtoList;

    }


}
