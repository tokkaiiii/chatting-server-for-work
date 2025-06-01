package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.command.MessageCommand;
import com.chat.chattingserverapp.chat.service.MessageService;
import com.chat.chattingserverapp.websocket.event.StompEventListener;
import java.security.Principal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public record MessageController(
    MessageService messageService,
    StompEventListener stompEventListener,
    SimpMessagingTemplate messagingTemplate
) {

  @MessageMapping("/send")
  public void send(@RequestBody MessageCommand command, Principal principal) {
    String message = command.message();

    log.info("send message: {}, sender {}", message, command.sender());
    UUID id = UUID.fromString(principal.getName());
    messageService.send(id, command.chatRoomId(), message);
    if (!Objects.equals(command.sender(), principal.getName())) {
      messagingTemplate.convertAndSend("/topic/room/" + command.chatRoomId(), command);
    }
  }

  @MessageMapping("/sessions")
  @SendToUser("/queue/sessions")
  public MessageCommand sessions(@RequestBody MessageCommand command, MessageHeaders headers,
      Principal principal) {
    String sessionId = headers.get("simpSessionId").toString();
    log.info("sessionId: {}", sessionId);
    String message = command.message();

    Set<String> sessions = stompEventListener.getSessions();
    log.info("send message: {}", message);
    UUID id = UUID.fromString(principal.getName());
    messageService.send(id, command.chatRoomId(), message);
    return command;
  }

}
