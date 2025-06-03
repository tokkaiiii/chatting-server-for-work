package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.admin.service.AdminService;
import com.chat.chattingserverapp.chat.response.ChatRoomListResponse;
import com.chat.chattingserverapp.chat.service.ChatRoomService;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public record ChatRoomListController(
    ChatRoomService chatRoomService,
    AdminService adminService
) {


  @GetMapping("/rooms")
  @ResponseBody
  public List<ChatRoomListResponse> getRooms(Principal principal) {
    UUID id = UUID.fromString(principal.getName());
    return chatRoomService.findAll(id);
  }

  @GetMapping("/rooms/for-admin")
  public ResponseEntity<?> getRoomsForAdmin(Principal principal) {
    UUID id = UUID.fromString(principal.getName());
    return adminService.findById(id)
        .map(adminResponse -> ResponseEntity.ok(chatRoomService.findAll()))
        .orElseGet(() -> ResponseEntity.status(401).build());
  }
}
