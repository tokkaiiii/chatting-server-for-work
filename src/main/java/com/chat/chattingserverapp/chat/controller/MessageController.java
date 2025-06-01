package com.chat.chattingserverapp.chat.controller;

import com.chat.chattingserverapp.chat.command.MessageCommand;
import com.chat.chattingserverapp.chat.service.MessageService;
import java.security.Principal;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public record MessageController(
    MessageService messageService
) {

  @MessageMapping("/send")
  @SendTo("/topic/room")
  public MessageCommand send(@RequestBody MessageCommand command, Principal principal) {
    String message = command.message();

    log.info("send message: {}", message);
    UUID id = UUID.fromString(principal.getName());
    messageService.send(id, command.chatRoomId(), message);
    return command;
  }

}
