package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.command.MessageCommand;
import com.chat.chattingserverapp.chat.service.MessageService;
import com.chat.chattingserverapp.websocket.event.StompEventListener;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public record MessageController(
    MessageService messageService,
    StompEventListener stompEventListener,
    SimpMessagingTemplate messagingTemplate
) {

  @MessageMapping("/chat.sendMessage")
  public void sendMessage(@RequestBody MessageCommand command, Principal principal) {
    log.info("send message: {}, sender {}", command.content(), command.sender());
    
    UUID id = UUID.fromString(principal.getName());
    messageService.send(id, command.roomId(), command.content());
    
    // 메시지를 채팅방의 모든 구독자에게 전송
    messagingTemplate.convertAndSend("/topic/chat.room." + command.roomId(), command);
  }

  @MessageMapping("/chat.addUser")
  @SendToUser("/queue/reply")
  public MessageCommand addUser(@RequestBody MessageCommand command, Principal principal) {
    log.info("User {} joined chat room {}", principal.getName(), command.roomId());
    return command;
  }

  @GetMapping("/chat/rooms/{roomId}/messages")
  public List<MessageCommand> getMessages(@PathVariable Long roomId) {
    log.info("Fetching messages for room: {}", roomId);
    return messageService.getMessages(roomId);
  }
}
