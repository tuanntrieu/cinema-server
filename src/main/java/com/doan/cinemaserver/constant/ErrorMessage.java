package com.doan.cinemaserver.constant;

public class ErrorMessage {
    public static final String ERR_EXCEPTION_GENERAL = "exception.general";
    public static final String UNAUTHORIZED = "exception.unauthorized";
    public static final String FORBIDDEN = "exception.forbidden";
    public static final String FORBIDDEN_UPDATE_DELETE = "exception.forbidden.update-delete";

    //error validation dto
    public static final String INVALID_SOME_THING_FIELD = "invalid.general";
    public static final String INVALID_FORMAT_SOME_THING_FIELD = "invalid.general.format";
    public static final String INVALID_SOME_THING_FIELD_IS_REQUIRED = "invalid.general.required";
    public static final String INVALID_NOT_BLANK_FIELD = "invalid.general.not-blank";
    public static final String INVALID_FORMAT_PASSWORD = "invalid.password-format";
    public static final String INVALID_FORMAT_EMAIL = "invalid.email-format";
    public static final String INVALID_FORMAT_PHONE = "invalid.phone-format";
    public static final String INVALID_DATE = "invalid.date-format";
    public static final String INVALID_DATE_FEATURE = "invalid.date_future";
    public static final String INVALID_DATETIME = "invalid.datetime_format";
    public static final String INVALID_REPEAT_PASSWORD = "invalid.password-repeat";

    public static final String INVALID_TOKEN_EXPIRED = "invalid.token-expired";
    public static final String INVALID_TOKEN = "exception.auth.invalid.token";

    public static class User {
        public static final String ERR_NOT_FOUND_USERNAME = "exception.user.not.found.username";
        public static final String ERR_NOT_FOUND_ID = "exception.user.not.found.id";
    }

    public static class Customer {
        public static final String ERR_NOT_FOUND_ID = "exception.customer.not.found.id";
        public static final String ERR_NOT_FOUND_EMAIL = "exception.customer.not.found.email";
    }

    public static class Auth {
        public static final String ERR_INCORRECT_USERNAME_PASSWORD = "exception.auth.incorrect.username-password";
        public static final String ERR_INVALID_REFRESH_TOKEN = "exception.auth.invalid.refresh_token";
        public static final String ERR_EXIST_EMAIL = "exception.auth.exist.email";
        public static final String ERR_INCORRECT_PASSWORD = "exception.auth.incorrect.password";
        public static final String ERR_NOT_EXIST_EMAIL = "exception.auth.not.exist.email";
        public static final String ERR_INCORRECT_OTP = "exception.auth.incorrect.otp";
        public static final String ERR_INVALID_VERIFY_STATUS = "exception.auth.invalid.verify_status";
        public static final String ERR_ACCOUNT_LOCKED="exception.auth.user.locked";
    }

    public static class Role {
        public static final String ERR_NOT_FOUND_ROLE = "exception.role.not.found.code";
    }

    public static class Cinema {
        public static final String ERR_NOT_FOUND_CINEMA = "exception.cinema.not.found.id";
    }

    public static class MovieType {
        public static final String ERR_NOT_FOUND_MOVIE_TYPE = "exception.movie-type.not.found.id";
    }

    public static class Schedule {
        public static final String ERR_NOT_FOUND_SCHEDULE = "exception.schedule.not.found.id";
        public static final String ERR_INTERVAL_CONFLICT = "exception.schedule.interval.conflict";
        public static final String ERR_UNRELEASED = "exception.schedule.unreleased";
        public static final String ERR_OVERTIME = "exception.schedule.overtime";
        public static final String ERR_TICKET_SOLD="exception.schedule.sold-ticket";
    }

    public static class Movie {
        public static final String ERR_NOT_FOUND_MOVIE = "exception.movie.not.found.id";
        public static final String ERR_INVALID_TIME = "exception.movie.invalid.time";
        public static final String ERR_INVALID_DURATION = "exception.movie.invalid.duration";
        public static final String ERR_CURRENTLY_SHOWING="exception.movie.currently.showing";
        public static final String ERR_PREVIOUS_SCHEDULE="exception.movie.previous.schedule";
        public static final String ERR_NEXT_SCHEDULE="exception.movie.next.schedule";
    }

    public static class Room {
        public static final String ERR_NOT_FOUND_ROOM = "exception.room.not.found.id";
        public static final String ERR_NOT_FOUND_ROOM_TYPE = "exception.room-type.not.found.id";
        public static final String ERR_HAS_SCHEDULE ="exception.room.has.schedule";
    }

    public static class Seat {
        public static final String ERR_NOT_FOUND_SEAT = "exception.seat.not.found.id";
        public static final String ERR_NOT_FOUND_SEAT_TYPE = "exception.seat-type.not.found.id";
        public static final String ERR_NOT_FOUND_COORDINATES = "exception.seat.not.found.coordinates";
        public static final String ERR_NOT_EMPTY_SEAT_OUTSIDE = "exception.seat.not.empty.outside";
        public static final String ERR_NOT_EMPTY_SEAT_MIDDLE = "exception.seat.not.empty.middle";
        public static final String ERR_INVALID_SEAT_STATUS = "exception.couple.invalid.status";
        public static final String ERR_SEAT_COUPLE_NEXT_ROW = "exception.seat.couple.next-row";
        public static final String ERR_SEAT_COUPLE_LAST_ROW = "exception.seat.couple.last-row";
        public static final String ERR_SEAT_COUPLE_TWO_LAST_ROW = "exception.seat.couple.two-last-row";
        public static final String ERR_SEAT_INVALID_ROW="exception.seat.invalid.row";
        public static final String ERR_SEAT_VIP_NOT_THE_FIRST_THREE_ROW="exception.seat.vip.not-the-first-three-row";
        public static final String ERR_SEAT_NEXT_ROW_STANDARD = "exception.seat.next-row-standard";
        public static final String ERR_SEAT_PREV_ROW_STANDARD="exception.seat.prev-row-must-standard";
    }

    public static class Combo {
        public static final String ERR_NOT_FOUND_COMBO = "exception.combo.not.found.id";
    }

    public static class Food {
        public static final String ERR_NOT_FOUND_FOOD = "exception.food.not.found.id";
    }

    public static class Payment {
        public static final String ERR_INVALID_OR_TAMPERED_DATA = "exception.payment.invalid.or.tampered.data";
        public static final String ERR_PAYMENT_TIMEOUT = "exception.payment.timeout";
    }
    public static class Ticket {
        public static final String ERR_NOT_FOUND_TICKET = "exception.ticket.not.found.id";
    }


}
