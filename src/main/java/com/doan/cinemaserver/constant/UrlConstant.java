package com.doan.cinemaserver.constant;

public class UrlConstant {
    public static class Auth {
        private static final String PRE_FIX = "/auth";

        public static final String LOGIN = PRE_FIX + "/login";
        public static final String REGISTER = PRE_FIX + "/register";
        public static final String LOGOUT = PRE_FIX + "/logout";
        public static final String FORGET_PASSWORD = PRE_FIX + "/forget-password";
        public static final String REFRESH_TOKEN = PRE_FIX + "/refresh-token";
        public static final String CHANGE_PASSWORD = PRE_FIX + "/change-password";
        public static final String SEND_OTP = PRE_FIX + "/send-otp";
        public static final String VERIFY_OTP = PRE_FIX + "/verify-otp";

        private Auth() {
        }
    }

    public static class Customer {
        public static final String PRE_FIX = "/customer";
        public static final String UPDATE_CUSTOMER = PRE_FIX + "/update";

        private Customer() {
        }
    }

    public static class Cinema {
        public static final String PRE_FIX = "/cinema";
        public static final String UPDATE_CINEMA = PRE_FIX + "/update" + "/{id}";
        public static final String CREATE_CINEMA = PRE_FIX + "/create";
        public static final String LOAD_ALL_PROVINCE = PRE_FIX + "/province";
        public static final String LOAD_CINEMA_BY_PROVINCE = PRE_FIX + "/load-by-province";

        private Cinema() {
        }
    }

    public static class MovieType {
        public static final String PRE_FIX = "/movie-type";
        public static final String CREATE_TYPE = PRE_FIX + "/create";
        public static final String UPDATE_TYPE = PRE_FIX + "/update" + "/{id}";

        private MovieType() {
        }
    }

    public static class Schedule {
        public static final String PRE_FIX = "/schedule";
        public static final String CREATE_SCHEDULE = PRE_FIX + "/create";
        public static final String DELETE_SCHEDULE = PRE_FIX + "/delete" + "/{id}";
        public static final String GET_SCHEDULES_BY_ROOM = PRE_FIX + "/get-by-room";
        public static final String GET_SCHEDULES_BY_CINEMA = PRE_FIX + "/get-by-cinema";

        private Schedule() {
        }
    }

    public static class Room {
        public static final String PRE_FIX = "/room";
        public static final String CREATE_ROOM = PRE_FIX + "/create";
        public static final String DELETE_ROOM = PRE_FIX + "/delete" + "/{id}";
        public static final String UPDATE_ROOM_TYPE = PRE_FIX + "/update-room-type/{id}";
        public static final String UPDATE_ROOM_SURCHARGE = PRE_FIX + "/update-room-surcharge/{id}";
        public static final String GET_ROOM_ORDER = PRE_FIX + "/get-room-order/{id}";

        private Room() {

        }
    }

    public static class Seat {
        public static final String PRE_FIX = "/seat";
        public static final String UPDATE_SEAT = PRE_FIX + "/update";
        public static final String DELETE_SEAT = PRE_FIX + "/delete";
        public static final String VALIDATE_SEATS = PRE_FIX + "/validate-seat/{id}";

        private Seat() {
        }
    }

    public static class Movie {
        public static final String PRE_FIX = "/movie";
        public static final String CREATE_MOVIE = PRE_FIX + "/create";
        public static final String SEARCH_MOVIE_BY_DATE = PRE_FIX + "/search-by-date";
        public static final String SEARCH_MOVIE_COMING_SOON = PRE_FIX + "/search-coming-soon";
    }

}
