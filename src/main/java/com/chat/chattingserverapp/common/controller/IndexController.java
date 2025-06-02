package com.chat.chattingserverapp.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public record IndexController() {

  @GetMapping("/")
  public String index(@RequestHeader(value = "Authorization", required = false) String header) {
    if (header != null && header.startsWith("Bearer ")) {
      return "redirect:chat/rooms";
    }
    return "redirect:client/login";
  }
}
