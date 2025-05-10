package com.doan.cinemaserver.constant;

public enum RoomTypeEnum {
    _2D(" 2D"),
    _3D(" 3D"),
    _4D(" 4D"),
    IMAX(" IMAX");

    private String value;

    RoomTypeEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
