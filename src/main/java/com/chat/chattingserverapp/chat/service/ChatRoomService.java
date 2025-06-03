package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.chat.response.ChatRoomListResponse;
import com.chat.chattingserverapp.chat.response.ChatRoomResponse;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomService {
  ChatRoomResponse createRoom(String name, UUID creatorId);
  List<ChatRoomResponse> findAllRooms();
  ChatRoomResponse findById(UUID roomId);
  void deleteRoom(UUID roomId);
  List<ChatRoomListResponse> findAll(UUID clientId);
  List<ChatRoomListResponse> findAll();

  Optional<ChatRoomResponse> createChatRoom(ChatRoomCreateCommand command, UUID id);
}
