package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.response.ChatRoomListResponse;
import com.chat.chattingserverapp.chat.service.ChatRoomService;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public record ChatRoomListController(
    ChatRoomService chatRoomService
) {


    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomListResponse> getRooms(Principal principal) {
        UUID id = UUID.fromString(principal.getName());
        List<ChatRoomListResponse> all = chatRoomService.findAll(id);
        return all;
    }
}
