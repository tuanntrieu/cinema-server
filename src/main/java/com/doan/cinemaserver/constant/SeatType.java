package com.doan.cinemaserver.constant;

public enum SeatType {
    STANDARD("Ghế thưởng"),
    VIP("Ghế vip"),
    COUPLE("Ghế đôi");

    private final String name;
    private SeatType(String name) {
        this.name=name;
    }
}
