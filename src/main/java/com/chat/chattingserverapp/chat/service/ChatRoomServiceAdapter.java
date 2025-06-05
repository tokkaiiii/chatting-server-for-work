package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.admin.service.AdminService;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomServiceAdapter implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ClientRepository clientRepository;
  private final MessageRepository messageRepository;
  private final AdminService adminService;

  @Override
  @Transactional
  public ChatRoomResponse createRoom(String name, UUID creatorId) {
    Client creator = clientRepository.findById(creatorId)
        .orElseThrow(() -> new IllegalArgumentException("Client not found"));

    ChatRoom chatRoom = ChatRoom.of(name, creator);
    Optional<ChatRoom> savedRoom = chatRoomRepository.save(chatRoom);

    if (savedRoom.isPresent()) {
      // 관리자 계정 가져오기
      Admin admin = adminService.getDefaultAdmin();
      // 관리자 할당
      savedRoom.get().assignAdmin(admin);
      // 환영 메시지 보내기
      messageRepository.send(admin, savedRoom.get(), "안녕하세요! 관리자입니다. 무엇을 도와드릴까요?");
    }

    return ChatRoomResponse.from(savedRoom.orElseThrow(() -> new IllegalStateException("Failed to create chat room")));
  }

  @Override
  public List<ChatRoomResponse> findAllRooms() {
    return chatRoomRepository.findAll().stream()
        .map(ChatRoomResponse::from)
        .toList();
  }



  @Override
  public ChatRoomResponse findById(UUID roomId) {
    return ChatRoomResponse.from(
        chatRoomRepository.findById(Long.valueOf(roomId.toString()))
            .orElseThrow(() -> new IllegalArgumentException("Chat room not found"))
    );
  }

  @Override
  @Transactional
  public void deleteRoom(UUID roomId) {
    chatRoomRepository.findById(Long.valueOf(roomId.toString()))
        .ifPresent(chatRoom -> chatRoomRepository.delete(chatRoom));
  }

  @Override
  public Optional<ChatRoomResponse> createChatRoom(ChatRoomCreateCommand command, UUID clientId) {
    Client client = getClientFrom(clientId);
    ChatRoom chatRoom = command.toChatRoom(client);
    Optional<ChatRoom> savedChatRoom = chatRoomRepository.save(chatRoom);
    
    if (savedChatRoom.isPresent()) {
      // 관리자 계정 가져오기
      Admin admin = adminService.getDefaultAdmin();
      // 관리자 할당
      savedChatRoom.get().assignAdmin(admin);
      // 어드민의 환영 메시지 추가
      messageRepository.send(admin, savedChatRoom.get(), "안녕하세요! 관리자입니다. 무엇을 도와드릴까요?");
    }
    
    return savedChatRoom.map(ChatRoomResponse::from);
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
          .orElseGet(() -> Message.of("메시지가 없습니다", chatRoom.getAdmin(), chatRoom));

      ChatRoomListResponse chatRoomListResponse = new ChatRoomListResponse(chatRoom.getId(),
          chatRoom.getRoomName(), message.getMessage(),
          message.getCreatedAt(), 0);
      list.add(chatRoomListResponse);
    }

    return list;
  }

  @Override
  public List<ChatRoomListResponse> findAll() {
    List<ChatRoom> chatRooms = chatRoomRepository.findAll();
    List<ChatRoomListResponse> list = new ArrayList<>();
    for (ChatRoom chatRoom : chatRooms) {
      Message message = messageRepository.findByChatRoom(chatRoom)
          .stream()
          .max(Comparator.comparing(Message::getCreatedAt))
          .orElseGet(() -> Message.of("메시지가 없습니다", chatRoom.getAdmin(), chatRoom));

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
