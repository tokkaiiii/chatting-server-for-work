package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.service.ChatRoomService;
import java.security.Principal;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public record ChatRoomCreateController(
    ChatRoomService chatRoomService
) {

  @PostMapping("/rooms")
  public String create(ChatRoomCreateCommand command, Principal principal) {
    UUID id = UUID.fromString(principal.getName());
    chatRoomService.createChatRoom(command, id);
    return "redirect:/chat/rooms";
  }
}
