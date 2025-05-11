package com.doan.cinemaserver.constant;

public enum SeatStatus {
    AVAILABLE("Ghế trống"),
    SOLD("Đã bán"),
    MAINTENANCE  ("Ghế bảo trì"),
    HOLDING("Được giữ"),
    SELECTED ("Được chọn");


    private final String value;

    SeatStatus(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}
