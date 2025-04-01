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

    public static class User{
        public static final String ERR_NOT_FOUND_USERNAME = "exception.user.not.found.username";
    }
    public static class Auth{
        public static final String ERR_INCORRECT_USERNAME_PASSWORD ="exception.auth.incorrect.username-password";
        public static final String ERR_INVALID_REFRESH_TOKEN ="exception.auth.invalid.refresh_token";
        public static final String ERR_EXIST_EMAIL ="exception.auth.exist.email";
        public static final String ERR_INCORRECT_PASSWORD ="exception.auth.incorrect.password";
        public static final String ERR_NOT_EXIST_EMAIL ="exception.auth.not.exist.email";
        public static final String ERR_INCORRECT_OTP="exception.auth.incorrect.otp";
    }
    public static class Role{
        public static final String ERR_NOT_FOUND_ROLE = "exception.role.not.found.role";
    }

}
