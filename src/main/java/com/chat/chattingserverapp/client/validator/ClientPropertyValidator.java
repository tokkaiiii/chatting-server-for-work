package com.chat.chattingserverapp.client.validator;

public class ClientPropertyValidator {

  public static final String USERNAME_REGEX = "^[가-힣0-9\\-_]{3,}$";

  public static boolean isValidUsername(String username) {
    return username != null
        && username.matches(USERNAME_REGEX);
  }

  public static boolean isValidPassword(String password) {
    return password != null
        && password.length() >= 8;
  }
}
