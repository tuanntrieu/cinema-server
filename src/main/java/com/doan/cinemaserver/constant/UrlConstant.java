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
        public static final String GET_CUSTOMER_INFOR = PRE_FIX;
        public static final String UPDATE_CUSTOMER = PRE_FIX + "/update";
        public static final String UPDATE_CUSTOMER_CINEMA = PRE_FIX + "/update-cinema";
        public static final String GET_CUSTOMER_CINEMA = PRE_FIX + "/get-cinema";
        public static final String GET_ALL_CUSTOMER= PRE_FIX + "/get-all";
        public static final String LOCK_ACCOUNT= PRE_FIX + "/lock/{id}";
        public static final String UN_LOCK_ACCOUNT= PRE_FIX + "/un-lock/{id}";

        private Customer() {
        }
    }

    public static class Cinema {
        public static final String PRE_FIX = "/cinema";
        public static final String UPDATE_CINEMA = PRE_FIX + "/update" + "/{id}";
        public static final String CREATE_CINEMA = PRE_FIX + "/create";
        public static final String LOAD_ALL_PROVINCE = PRE_FIX + "/province";
        public static final String LOAD_CINEMA_BY_PROVINCE = PRE_FIX + "/load-by-province";
        public static final String LOAD_ALL_CINEMA = PRE_FIX + "/load-all-cinema";
        public static final String GET_ALL_CINEMA = PRE_FIX + "/get-all";
        public static final String GET_CINEMA_DETAIL = PRE_FIX + "/{id}";
        public static final String GET_ROOM_BY_CINEMA = PRE_FIX + "/get-room";

        private Cinema() {
        }
    }

    public static class MovieType {
        public static final String PRE_FIX = "/movie-type";
        public static final String CREATE_TYPE = PRE_FIX + "/create";
        public static final String UPDATE_TYPE = PRE_FIX + "/update" + "/{id}";
        public static final String DELETE_TYPE = PRE_FIX + "/delete" + "/{id}";
        public static final String GET_MOVIE_TYPE = PRE_FIX + "/get-all";
        public static final String GET_MOVIE_TYPE_PAGE = PRE_FIX + "/get-all-page";

        private MovieType() {
        }
    }

    public static class Schedule {
        public static final String PRE_FIX = "/schedule";
        public static final String CREATE_SCHEDULE = PRE_FIX + "/create";
        public static final String DELETE_SCHEDULE = PRE_FIX + "/delete" + "/{id}";
        public static final String GET_SCHEDULES_BY_ROOM = PRE_FIX + "/get-by-room";
        public static final String GET_SCHEDULES_FOR_MOVIE_BY_CINEMA = PRE_FIX + "/get-by-cinema";
        public static final String GET_SCHEDULES_FOR_MOVIE_BY_DATE = PRE_FIX + "/get-by-date";

        private Schedule() {
        }
    }

    public static class Room {
        public static final String PRE_FIX = "/room";
        public static final String CREATE_ROOM = PRE_FIX + "/create";
        public static final String DELETE_ROOM = PRE_FIX + "/delete" + "/{id}";
        public static final String UPDATE_ROOM_TYPE = PRE_FIX + "/update-room-type/{id}";
        public static final String UPDATE_ROOM_SURCHARGE = PRE_FIX + "/update-room-surcharge";
        public static final String GET_ROOM_ORDER = PRE_FIX + "/get-room-order/{id}";
        public static final String GET_ALL_ROOM_TYPES = PRE_FIX + "/get-all-room-types";
        public static final String VALIDATE_ROOM = PRE_FIX + "/validate/{id}";
        public static final String GET_ROOM_DETAIL = PRE_FIX + "/{id}";

        private Room() {

        }
    }

    public static class Seat {
        public static final String PRE_FIX = "/seat";
        public static final String UPDATE_SEAT_PRICE = PRE_FIX + "/update-seat-price";
        public static final String UPDATE_SEAT_STATUS = PRE_FIX + "/update-status";
        public static final String HOLD_SEAT = PRE_FIX + "/hold-seat";
        public static final String UNHOLD_SEAT = PRE_FIX + "/unhold-seat";
        public static final String DELETE_SEAT = PRE_FIX + "/delete";
        public static final String VALIDATE_SEATS = PRE_FIX + "/validate-seat/{id}";
        public static final String GET_ALL_SEAT_PRICE = PRE_FIX + "/get-all-seat-price";
        public static final String UPDATE_PRICE = PRE_FIX + "/update-price";
        public static final String MAINTAIN_SEATS = PRE_FIX + "/maintain";
        public static final String UN_MAINTAIN_SEATS = PRE_FIX + "/un-maintain";
        public static final String UPDATE_VIP_SEAT = PRE_FIX + "/update-vip";
        public static final String UPDATE_COUPLE_SEAT = PRE_FIX + "/update-couple";
        public static final String UPDATE_STANDARD_SEAT = PRE_FIX + "/update-standard";

        private Seat() {
        }
    }

    public static class Movie {
        public static final String PRE_FIX = "/movie";
        public static final String GET_MOVIE_DETAIL = PRE_FIX + "/{id}";
        public static final String CREATE_MOVIE = PRE_FIX + "/create";
        public static final String SEARCH_MOVIE_BY_DATE = PRE_FIX + "/search-by-date";
        public static final String SEARCH_MOVIE_COMING_SOON = PRE_FIX + "/search-coming-soon";
        public static final String GET_ALL_MOVIE = PRE_FIX + "/get-all";
        public static final String UPDATE_MOVIE = PRE_FIX + "/update/{id}";
        public static final String DELETE_MOVIE = PRE_FIX + "/delete/{id}";
        public static final String GET_MOVIE_SCHEDULE=PRE_FIX+"/schedule";
    }

    public static class Ticket {
        public static final String PRE_FIX = "/ticket";
        public static final String CHECKOUT = PRE_FIX + "/checkout";
        public static final String GET_TICKETS_BY_CUSTOMER = PRE_FIX + "/get-tickets-by-customer";
        public static final String GET_PAYMENT_URL = PRE_FIX + "/payment-url";
        public static final String HANDLE_VNPAY_RETURN = PRE_FIX + "/handle-return";
        public static final String SAVE_DATA_TMP = PRE_FIX + "/save-data-tmp";
        public static final String READ_DATA_TMP = PRE_FIX + "/read-data-tmp";
        public static final String DELETE_DATA_TMP = PRE_FIX + "/delete-data-tmp";
        public static final String EXIST_BY_ID = PRE_FIX + "/exist-by-id";
        public static final String GET_TICKET_DETAIL = PRE_FIX + "/{id}";
    }

    public static class Combo {
        public static final String PRE_FIX = "/combo";
        public static final String CREATE_COMBO = PRE_FIX + "/create";
        public static final String GET_COMBO = PRE_FIX + "/get-all";
        public static final String DELETE_COMBO = PRE_FIX + "/delete/{id}";
    }

    public static class Food {
        public static final String PRE_FIX = "/food";
        public static final String CREATE_FOOD = PRE_FIX + "/create";
        public static final String GET_FOOD = PRE_FIX + "/get-all";
        public static final String GET_FOOD_PAGE = PRE_FIX + "/get-all-page";
        public static final String DELETE_FOOD = PRE_FIX + "/delete/{id}";
    }

    public static class Statistics {
        public static final String PRE_FIX = "/statistics";
        public static final String REVENUE_CINEMA = PRE_FIX + "/revenue-cinema";
        public static final String REVENUE_MOVIE = PRE_FIX + "/revenue-movie";
        public static final String REVENUE_CHART_BY_MONTH = PRE_FIX + "/revenue-by-month";
        public static final String REVENUE_CHART_BY_YEAR = PRE_FIX + "/revenue-by-year";
        public static final String COUNT_CUSTOMER_BY_DATE = PRE_FIX + "/count-customer-date";
        public static final String COUNT_CUSTOMER_BY_WEEK = PRE_FIX + "/count-customer-week";
        public static final String COUNT_CUSTOMER_BY_MONTH = PRE_FIX + "/count-customer-month";
        public static final String COUNT_TICKET_BY_DATE = PRE_FIX + "/count-ticket-date";
        public static final String COUNT_TICKET_BY_WEEK = PRE_FIX + "/count-ticket-week";
        public static final String COUNT_TICKET_BY_MONTH = PRE_FIX + "/count-ticket-month";
        public static final String SUM_TOTAL_BY_DATE = PRE_FIX + "/sum-total-date";
        public static final String SUM_TOTAL_BY_WEEK = PRE_FIX + "/sum-total-week";
        public static final String SUM_TOTAL_BY_MONTH = PRE_FIX + "/sum-total-month";
        public static final String EXPORT_MOVIE_EXCEL = PRE_FIX + "/export-movie-excel";
        public static final String EXPORT_CINEMA_EXCEL = PRE_FIX + "/export-cinema-excel";
    }


}
