package edu.rs.raf.userservice.dto;

public class RankDto {
    private Long id;
    private String name;
    private int reservationNumMin;
    private int discount;

    public RankDto(Long id, String name, int reservationNumMin, int discount) {
        this.id = id;
        this.name = name;
        this.reservationNumMin = reservationNumMin;
        this.discount = discount;
    }

    public RankDto() {
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

    public int getReservationNumMin() {
        return reservationNumMin;
    }

    public void setReservationNumMin(int reservationNumMin) {
        this.reservationNumMin = reservationNumMin;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
