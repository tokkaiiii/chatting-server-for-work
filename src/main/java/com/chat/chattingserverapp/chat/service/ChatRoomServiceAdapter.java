package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.chat.domain.Message;
import com.chat.chattingserverapp.chat.infrastructure.ChatRoomRepository;
import com.chat.chattingserverapp.chat.infrastructure.MessageRepository;
import com.chat.chattingserverapp.chat.response.ChatRoomListResponse;
import com.chat.chattingserverapp.chat.response.ChatRoomResponse;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.client.infrastructure.ClientRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceAdapter implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ClientRepository clientRepository;
  private final MessageRepository messageRepository;

  @Override
  public Optional<ChatRoomResponse> createChatRoom(ChatRoomCreateCommand command, UUID clientId) {
    return chatRoomRepository.save(command.toChatRoom(getClientFrom(clientId)))
        .map(ChatRoomResponse::from);
  }

  @Override
  public List<ChatRoomListResponse> findAll(UUID clientId) {
    Client client = clientRepository.findById(clientId).orElseThrow();
    List<ChatRoom> chatRooms = chatRoomRepository.findByClient(client);
    List<ChatRoomListResponse> list = new ArrayList<>();
    for (ChatRoom chatRoom : chatRooms) {
      Message message = messageRepository.findByChatRoom(chatRoom)
          .stream()
          .max(Comparator.comparing(Message::getCreatedAt))
          .orElse(Message.of("메시지가 없습니다", null, chatRoom));

      ChatRoomListResponse chatRoomListResponse = new ChatRoomListResponse(chatRoom.getId(),
          chatRoom.getRoomName(), message.getMessage(),
          message.getCreatedAt(), 0);
      list.add(chatRoomListResponse);
    }

    return list;
  }

  private Client getClientFrom(UUID clientId) {
    return clientRepository.findById(clientId)
        .orElseThrow(() -> new IllegalArgumentException("Client not found: " + clientId));
  }
}
