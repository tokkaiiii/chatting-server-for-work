package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.service.ChatRoomService;
import java.security.Principal;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public record ChatRoomCreateController(
    ChatRoomService chatRoomService
) {

  @PostMapping("/rooms")
  public ResponseEntity<?> create(@RequestBody ChatRoomCreateCommand command, Principal principal) {
    UUID id = UUID.fromString(principal.getName());

    if (command.roomName() == null || command.roomName().isBlank()) {
      return ResponseEntity.badRequest().build();
    }

    return chatRoomService.createChatRoom(command, id)
        .map(chatRoomResponse -> ResponseEntity.status(201).body(chatRoomResponse))
        .orElseGet(ResponseEntity.badRequest()::build);
  }
}
