package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.response.ChatRoomListResponse;
import com.chat.chattingserverapp.chat.service.ChatRoomService;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ChatRoomListController(
    ChatRoomService chatRoomService
) {

  @GetMapping("/chat/rooms")
  public ResponseEntity<?> list(Principal principal) {
    UUID id = UUID.fromString(principal.getName());
    List<ChatRoomListResponse> a = chatRoomService.findAll(id);
    return ResponseEntity.ok(a);
  }

}
