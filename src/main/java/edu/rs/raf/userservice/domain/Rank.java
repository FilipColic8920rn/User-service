package edu.rs.raf.userservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int reservationNumMin;
    private int discount;

    public Rank(String name, int discount, int reservationNumMin) {
        this.name = name;
        this.discount = discount;
        this.reservationNumMin = reservationNumMin;
    }

    public Rank() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getReservationNumMin() {
        return reservationNumMin;
    }

    public void setReservationNum(int reservationNumMin) {
        this.reservationNumMin = reservationNumMin;
    }
}
