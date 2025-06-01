package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.response.ChatRoomListResponse;
import com.chat.chattingserverapp.chat.response.ChatRoomResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomService {

  Optional<ChatRoomResponse> createChatRoom(ChatRoomCreateCommand command, UUID clientId);

  List<ChatRoomListResponse> findAll(UUID clientId);
}
