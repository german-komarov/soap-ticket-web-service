package com.german.soapwebservice.repositories;

import com.german.soapwebservice.entiites.Ticket;
import localhost._8080.tickets.SalonClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("select ticket from Ticket ticket " +
            "where " +
            "lower(ticket.flyFrom) like %:paramFrom% " +
            "and " +
            "lower(ticket.flyTo) like %:paramTo% " +
            "and " +
            "lower(ticket.airline) like %:paramAirline% " +
            "and " +
            "ticket.price <= :paramMaxPrice " +
            "and " +
            "ticket.salonClass in :paramSalonClasses " +
            "and " +
            "ticket.flyDateTime >= :paramMinDateTime " +
            "and " +
            "ticket.flyDateTime <= :paramMaxDateTime")
    Slice<Ticket> findTicketsByAllAttributes(@Param("paramFrom") String from,
                                             @Param("paramTo") String to,
                                             @Param("paramAirline") String airline,
                                             @Param("paramMaxPrice") Float maxPrice,
                                             @Param("paramSalonClasses") List<SalonClass> salonClasses,
                                             @Param("paramMinDateTime") LocalDateTime minDateTime,
                                             @Param("paramMaxDateTime") LocalDateTime maxDateTime,
                                             Pageable pageable);


}
