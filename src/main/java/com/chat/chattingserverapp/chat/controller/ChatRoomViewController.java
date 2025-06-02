package com.chat.chattingserverapp.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/view/chat")
@Controller
public record ChatRoomViewController() {

  @GetMapping("/rooms")
  public String rooms() {
    return "chat-room-list";
  }

  @GetMapping("/rooms/create")
  public String createRoom() {
    return "chat-room-create";
  }

  @GetMapping("/rooms/{chatRoomId}")
  public String joinRoom() {
    return "chat-room";
  }

}
