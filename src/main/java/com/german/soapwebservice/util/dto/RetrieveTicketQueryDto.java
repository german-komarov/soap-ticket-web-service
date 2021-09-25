package com.german.soapwebservice.util.dto;

import localhost._8080.tickets.SalonClass;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class RetrieveTicketQueryDto {


    private String from;
    private String to;
    private String airline;
    private Float maximumPrice;
    private List<SalonClass> salonClasses;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private Pageable pageable;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Float getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(Float maximumPrice) {
        this.maximumPrice = maximumPrice;
    }


    public List<SalonClass> getSalonClasses() {
        return salonClasses;
    }

    public void setSalonClasses(List<SalonClass> salonClasses) {
        this.salonClasses = salonClasses;
    }

    public LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(LocalDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public LocalDateTime getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(LocalDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
