package com.german.soapwebservice.entiites;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String username;

    private String password;

    @ElementCollection
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    private Set<Role> roles = new HashSet<>();


    private float balance;


    @ElementCollection
    @CollectionTable(name = "users_tickets_bought_place_numbers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "ticket_id")
    @MapKeyColumn(name = "bought_place_number")
    private Map<Integer, Long> boughtTickets = new HashMap<>();

    public boolean addRole(Role role) {
        boolean iaAdded = this.getRoles().add(role);

        return iaAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Map<Integer, Long> getBoughtTickets() {
        return boughtTickets;
    }

    public void setBoughtTickets(Map<Integer, Long> boughtTickets) {
        this.boughtTickets = boughtTickets;
    }

    public enum Role {
        CLIENT, EMPLOYEE;
    }
}
