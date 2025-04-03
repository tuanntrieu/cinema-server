package com.doan.cinemaserver.constant;

import com.doan.cinemaserver.domain.entity.Schedule;

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
    public static class Customer{
        public static final String PRE_FIX = "/customer";
        public static final String UPDATE_CUSTOMER = PRE_FIX + "/update-customer";

        private Customer() {}
    }
    public static class Cinema{
        public static final String PRE_FIX = "/cinema";
        public static final String UPDATE_CINEMA = PRE_FIX + "/update-cinema"+"/{id}";
        public static final String CREATE_CINEMA = PRE_FIX + "/create-cinema";
        public static final String LOAD_ALL_PROVINCE = PRE_FIX + "/province";
        public static final String LOAD_CINEMA_BY_PROVINCE = PRE_FIX + "/load-by-province";
        private Cinema() {}
    }
    public static class MovieType{
        public static final String PRE_FIX = "/movie-type";
        public static final String CREATE_TYPE=PRE_FIX + "/create-type";
        public static final String UPDATE_TYPE=PRE_FIX + "/update-type"+"/{id}";

        private MovieType() {}
    }
    public static class Schedule{
        public static final String PRE_FIX = "/schedule";
        public static final String CREATE_SCHEDULE=PRE_FIX + "/create-schedule";
        public static final String DELETE_SCHEDULE=PRE_FIX + "/delete-schedule"+"/{id}";
        public static final String GET_SCHEDULES=PRE_FIX + "/schedules";


        private Schedule() {}
    }
}
