package com.chat.chattingserverapp.chat.service;

import java.util.UUID;

public interface MessageService {

  void send(UUID clientId, Long chatRoomId, String message);

}
