package com.german.soapwebservice.entiites;


import localhost._8080.tickets.CreateTicketDto;
import localhost._8080.tickets.SalonClass;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tickets")
public class Ticket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String flyFrom;
    private String flyTo;
    private String airline;
    private float price;
    private int numberOfPlaces;
    private SalonClass salonClass;
    private LocalDateTime flyDateTime;


    @ElementCollection
    @JoinTable(name = "tickets_bought_place_numbers")
    @Column(name = "place_number")
    private Set<Integer> boughtPlaceNumbers = new HashSet<>();


    public static Ticket of(CreateTicketDto createTicketDto) {

        Ticket ticket = new Ticket();

        ticket.setFlyFrom(createTicketDto.getFrom());
        ticket.setFlyTo(createTicketDto.getTo());
        ticket.setAirline(createTicketDto.getAirline());
        ticket.setPrice(createTicketDto.getPrice());
        ticket.setNumberOfPlaces(createTicketDto.getNumberOfPlaces());
        ticket.setSalonClass(createTicketDto.getSalonClass());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime dateTime = LocalDateTime.parse(createTicketDto.getDateTime(), formatter);

        ticket.setFlyDateTime(dateTime);


        return ticket;


    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlyFrom() {
        return flyFrom;
    }

    public void setFlyFrom(String flyFrom) {
        this.flyFrom = flyFrom;
    }

    public String getFlyTo() {
        return flyTo;
    }

    public void setFlyTo(String flyTo) {
        this.flyTo = flyTo;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public void setNumberOfPlaces(int numberOfPlaces) {
        this.numberOfPlaces = numberOfPlaces;
    }

    public SalonClass getSalonClass() {
        return salonClass;
    }

    public void setSalonClass(SalonClass salonClass) {
        this.salonClass = salonClass;
    }

    public LocalDateTime getFlyDateTime() {
        return flyDateTime;
    }

    public void setFlyDateTime(LocalDateTime flyDateTime) {
        this.flyDateTime = flyDateTime;
    }

    public Set<Integer> getBoughtPlaceNumbers() {
        return boughtPlaceNumbers;
    }

    public void setBoughtPlaceNumbers(Set<Integer> boughtPlaceNumbers) {
        this.boughtPlaceNumbers = boughtPlaceNumbers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return getId() == ticket.getId() &&
                Float.compare(ticket.getPrice(), getPrice()) == 0 &&
                getNumberOfPlaces() == ticket.getNumberOfPlaces() &&
                Objects.equals(getFlyFrom(), ticket.getFlyFrom()) &&
                Objects.equals(getFlyTo(), ticket.getFlyTo()) &&
                Objects.equals(getAirline(), ticket.getAirline()) &&
                getSalonClass() == ticket.getSalonClass() &&
                Objects.equals(getFlyDateTime(), ticket.getFlyDateTime()) &&
                Objects.equals(getBoughtPlaceNumbers(), ticket.getBoughtPlaceNumbers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFlyFrom(), getFlyTo(), getAirline(), getPrice(), getNumberOfPlaces(), getSalonClass(), getFlyDateTime(), getBoughtPlaceNumbers());
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", flyFrom='" + flyFrom + '\'' +
                ", flyTo='" + flyTo + '\'' +
                ", airline='" + airline + '\'' +
                ", price=" + price +
                ", numberOfPlaces=" + numberOfPlaces +
                ", salonClass=" + salonClass +
                ", flyDateTime=" + flyDateTime +
                ", boughtPlaceNumbers=" + boughtPlaceNumbers +
                '}';
    }
}
