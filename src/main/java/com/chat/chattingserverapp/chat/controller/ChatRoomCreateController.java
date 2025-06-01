package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.service.ChatRoomService;
import java.security.Principal;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ChatRoomCreateController(
    ChatRoomService chatRoomService
) {

  @PostMapping("/chat/rooms")
  public ResponseEntity<?> createRoom(@RequestBody ChatRoomCreateCommand command, Principal principal) {
    if (command.roomName() == null) {
      return ResponseEntity.badRequest().build();
    }

    UUID id = UUID.fromString(principal.getName());
   return chatRoomService.createChatRoom(command, id)
        .map(chatRoomResponse -> ResponseEntity.created(null).body(chatRoomResponse))
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

}
