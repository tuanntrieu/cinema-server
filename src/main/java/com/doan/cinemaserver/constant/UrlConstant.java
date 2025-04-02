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
    public static class Customer{
        public static final String PRE_FIX = "/customer";
        public static final String UPDATE_CUSTOMER = PRE_FIX + "/update-customer";

        private Customer() {}
    }
    public static class Cinema{
        public static final String PRE_FIX = "/cinema";
        public static final String UPDATE_CINEMA = PRE_FIX + "/update-cinema"+"/{id}";
        public static final String CREATE_CINEMA = PRE_FIX + "/create-cinema";
        private Cinema() {}
    }
}
