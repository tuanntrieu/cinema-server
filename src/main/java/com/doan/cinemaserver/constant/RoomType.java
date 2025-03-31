package com.doan.cinemaserver.constant;

public enum RoomType {
    _2D("Phòng 2D"),
    _3D("Phòng 3D"),
    _4D("Phòng 4D"),
    IMAX("Phòng IMAX");

    private String value;

    RoomType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
