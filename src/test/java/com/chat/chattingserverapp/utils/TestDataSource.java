package com.chat.chattingserverapp.utils;

public class TestDataSource {

  public static String[] invalidUsernames() {
    return new String[]{
        "",
        "유저",
        "유저 ",
        "use",
    };
  }

  public static String[] validUsernames() {
    return new String[]{
        "유저이",
        "유저-이",
        "유저_이",
        "유저이123",
        "유저이-123",
        "유저이_123",
        "123",
        "---",
        "___",
    };
  }

  public static String[] invalidPasswords() {
    return new String[]{
        "",
        "pass",
        "pass123"
    };
  }

  public static String[] validPasswords() {
    return new String[]{
        "password123",
        "securePassword!@#",
        "P@ssw0rd2023"
    };
  }

}
