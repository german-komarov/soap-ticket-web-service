package com.german.soapwebservice.util.parsers;

import com.german.soapwebservice.exceptions.ParsingException;
import com.german.soapwebservice.exceptions.WrongDateTimeFormatException;
import com.german.soapwebservice.exceptions.WrongPageDataException;
import com.german.soapwebservice.util.dto.RetrieveTicketQueryDto;
import localhost._8080.tickets.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class RetrieveTicketRequestParser {


    public RetrieveTicketQueryDto parse(RequestTicketDto requestTicketDto) throws ParsingException {


        String from = requestTicketDto.getFrom();

        if (from == null) {
            from = "";
        } else {
            from = from.toLowerCase();
        }


        String to = requestTicketDto.getTo();

        if (to == null) {
            to = "";
        } else {
            to = to.toLowerCase();
        }


        String airline = requestTicketDto.getAirline();


        if (airline == null) {
            airline = "";
        } else {
            airline = airline.toLowerCase();
        }


        float maximumPrice = requestTicketDto.getMaxPrice();

        if (maximumPrice <= 0.0f) {
            maximumPrice = Float.MAX_VALUE;
        }


        List<SalonClass> salonClasses = this.convertToSalonClassList(requestTicketDto.getSalonClass());

        LocalDateTime fromDateTime;

        try {
            fromDateTime = this.convertToMinDateTime(requestTicketDto.getMinDateTime());
        } catch (WrongDateTimeFormatException e) {
            throw new ParsingException(e.getMessage());
        }

        LocalDateTime toDateTime;

        try {
            toDateTime = this.convertToMaxDateTime(requestTicketDto.getMaxDateTime());
        } catch (WrongDateTimeFormatException e) {
            throw new ParsingException(e.getMessage());
        }


        int pageNumber = requestTicketDto.getPageNumber();
        int pageSize = requestTicketDto.getPageSize();
        SortBy sortBy = requestTicketDto.getSortBy();
        SortDirection sortDirection = requestTicketDto.getSortDirection();


        Pageable pageable;

        try {
            pageable = this.createPageable(pageNumber, pageSize, sortBy, sortDirection);
        } catch (WrongPageDataException e) {
            throw new ParsingException(e.getMessage());
        }


        RetrieveTicketQueryDto queryDto = new RetrieveTicketQueryDto();

        queryDto.setFrom(from);
        queryDto.setTo(to);
        queryDto.setAirline(airline);
        queryDto.setMaximumPrice(maximumPrice);
        queryDto.setSalonClasses(salonClasses);
        queryDto.setFromDateTime(fromDateTime);
        queryDto.setToDateTime(toDateTime);
        queryDto.setPageable(pageable);


        return queryDto;

    }


    private List<SalonClass> convertToSalonClassList(SalonClassRequest salonClassRequest) {

        List<SalonClass> salonClasses = new ArrayList<>();


        if (salonClassRequest == null) {
            salonClasses.add(SalonClass.ECONOMY);
            salonClasses.add(SalonClass.BUSINESS);
            salonClasses.add(SalonClass.FIRST);

            return salonClasses;

        }


        switch (salonClassRequest) {

            case ECONOMY:
                salonClasses.add(SalonClass.ECONOMY);
                break;

            case BUSINESS:
                salonClasses.add(SalonClass.BUSINESS);
                break;

            case FIRST:
                salonClasses.add(SalonClass.FIRST);
                break;

            case ECONOMY_BUSINESS:
                salonClasses.add(SalonClass.ECONOMY);
                salonClasses.add(SalonClass.BUSINESS);
                break;

            case BUSINESS_FIRST:
                salonClasses.add(SalonClass.BUSINESS);
                salonClasses.add(SalonClass.FIRST);
                break;

            case ECONOMY_FIRST:
                salonClasses.add(SalonClass.ECONOMY);
                salonClasses.add(SalonClass.FIRST);
                break;

            case ANY:
                salonClasses.add(SalonClass.ECONOMY);
                salonClasses.add(SalonClass.BUSINESS);
                salonClasses.add(SalonClass.FIRST);
                break;

        }


        return salonClasses;


    }


    private LocalDateTime convertToMinDateTime(String dateTimeString) throws WrongDateTimeFormatException {

        LocalDateTime dateTime;

        if (dateTimeString == null || dateTimeString.isEmpty()) {

            dateTime = LocalDateTime.now();
            return dateTime;
        }

        String pattern = "yyyy-MM-dd HH:mm";

        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            dateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);
        } catch (Exception e) {
            throw new WrongDateTimeFormatException(String.format("Date-Time string is not valid, use pattern %s", pattern));
        }

        return dateTime;


    }


    private LocalDateTime convertToMaxDateTime(String dateTimeString) throws WrongDateTimeFormatException {

        LocalDateTime dateTime;

        if (dateTimeString == null || dateTimeString.isEmpty()) {

            dateTime = LocalDateTime.now();

            dateTime = LocalDateTime.of(dateTime.getYear() + 10, 1, 1, 0, 0);
            return dateTime;
        }

        String pattern = "yyyy-MM-dd HH:mm";

        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            dateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);
        } catch (Exception e) {
            throw new WrongDateTimeFormatException(String.format("Date-Time string is not valid, use pattern %s", pattern));
        }

        return dateTime;


    }


    private Pageable createPageable(int pageNumber, int pageSize, SortBy sortBy, SortDirection sortDirectionRequest) throws WrongPageDataException {


        if (pageNumber < 0) {
            throw new WrongPageDataException("Page number cannot be less than 0 (zero)");
        }

        if (pageSize < 3) {
            throw new WrongPageDataException("Page size cannot be less than 3 (three)");
        }


        String sortProperty = "";


        if (sortBy == null) {
            sortProperty = "flyDateTime";
        } else {

            switch (sortBy) {

                case DATE:
                    sortProperty = "flyDateTime";
                    break;

                case PRICE:
                    sortProperty = "price";
                    break;

                case SALON_CLASS:
                    sortProperty = "salonClass";
                    break;

                case NONE:
                    sortProperty = "";
                    break;


            }
        }


        Sort.Direction sortDirection;

        if (sortDirectionRequest == SortDirection.DESC) {
            sortDirection = Sort.Direction.DESC;
        } else {
            sortDirection = Sort.Direction.ASC;
        }


        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortDirection, sortProperty);


        return pageable;


    }


}
