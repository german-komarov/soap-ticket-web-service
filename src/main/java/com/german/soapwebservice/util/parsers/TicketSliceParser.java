package com.german.soapwebservice.util.parsers;


import com.german.soapwebservice.entiites.Ticket;
import localhost._8080.tickets.ResponseTicketDto;
import localhost._8080.tickets.RetrieveTicketResponse;
import localhost._8080.tickets.SortBy;
import localhost._8080.tickets.SortDirection;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class TicketSliceParser {


    public RetrieveTicketResponse parse(Slice<Ticket> ticketSlice) {

        RetrieveTicketResponse retrieveTicketResponse = new RetrieveTicketResponse();

        retrieveTicketResponse.getTicket().addAll(ticketSlice.map(this::convertToResponseTicketDto).getContent());

        retrieveTicketResponse.setCurrentPage(ticketSlice.getNumber());
        retrieveTicketResponse.setHasNext(ticketSlice.hasNext());

        Sort sort = ticketSlice.getSort();

        Optional<Sort.Order> order = sort.get().findFirst();

        order.ifPresent((o) -> {
            retrieveTicketResponse.setSortedDirection(SortDirection.valueOf(o.getDirection().name()));
            retrieveTicketResponse.setSortedBy(this.determineSortBy(o.getProperty()));
        });


        return retrieveTicketResponse;
    }


    private ResponseTicketDto convertToResponseTicketDto(Ticket ticket) {

        ResponseTicketDto responseTicketDto = new ResponseTicketDto();

        responseTicketDto.setId(ticket.getId());
        responseTicketDto.setFrom(ticket.getFlyFrom());
        responseTicketDto.setTo(ticket.getFlyTo());
        responseTicketDto.setAirline(ticket.getAirline());
        responseTicketDto.setPrice(ticket.getPrice());
        responseTicketDto.setSalonClass(ticket.getSalonClass());
        responseTicketDto.setDateTime(ticket.getFlyDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        responseTicketDto.getFreePlaces().addAll(this.determineFreePlaces(ticket.getNumberOfPlaces(), ticket.getBoughtPlaceNumbers()));

        return responseTicketDto;


    }


    private List<Integer> determineFreePlaces(int numberOfPlaces, Set<Integer> boughtPlaces) {

        List<Integer> integers = new ArrayList<>();

        for (int i = 1; i <= numberOfPlaces; i++) {

            if (!boughtPlaces.contains(i)) {
                integers.add(i);
            }
        }

        return integers;

    }


    private SortBy determineSortBy(String property) {

        if (property == null) {
            return SortBy.NONE;
        }


        switch (property) {

            case "flyDateTime":
                return SortBy.DATE;
            case "price":
                return SortBy.PRICE;
            case "salonClass":
                return SortBy.SALON_CLASS;
            default:
                return SortBy.NONE;

        }


    }


}
