package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.chat.command.MessageCommand;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  void send(UUID clientId, Long chatRoomId, String message);

  List<MessageCommand> getMessages(Long chatRoomId);
}
