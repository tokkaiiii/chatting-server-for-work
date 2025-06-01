package com.chat.chattingserverapp.client.validator;

public class ClientPropertyValidator {

  public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
  public static final String USERNAME_REGEX = "^[가-힣0-9\\-_]{3,}$";

  public static boolean isEmailValid(String email) {
    return email != null && email.matches(EMAIL_REGEX);
  }

  public static boolean isValidUsername(String username) {
    return username != null
        && username.matches(USERNAME_REGEX);
  }

  public static boolean isValidPassword(String password) {
    return password != null
        && password.length() >= 8;
  }
}
