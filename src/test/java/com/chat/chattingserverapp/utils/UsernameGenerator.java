package com.chat.chattingserverapp.utils;

import java.util.Random;

public class UsernameGenerator {
  public static String generateUsername() {
    return "유저" + new Random().nextInt(100);
  }

}
