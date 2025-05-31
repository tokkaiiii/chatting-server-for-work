package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ChatRoomCreateController(
    ChatRoomService chatRoomService
) {

  @PostMapping("/chat/rooms")
  public ResponseEntity<?> createRoom(@RequestBody ChatRoomCreateCommand command) {
    if (command.roomName() == null) {
      return ResponseEntity.badRequest().build();
    }

   return chatRoomService.createChatRoom(command)
        .map(chatRoomResponse -> ResponseEntity.created(null).body(chatRoomResponse))
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

}
