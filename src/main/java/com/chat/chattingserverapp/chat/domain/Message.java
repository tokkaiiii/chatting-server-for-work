package com.chat.chattingserverapp.chat.domain;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.admin.domain.Admin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
public class Message {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Getter
  private Long id;

  @Column(nullable = false)
  @Getter
  private String message;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "client_id")
  private Client sender;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "admin_id")
  @Getter
  private Admin adminSender;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "chat_room_id")
  @Getter
  private ChatRoom chatRoom;

  @Column(nullable = false)
  @Getter
  private LocalDateTime createdAt;

  protected Message() {
  }

  private Message(String message, Client sender, ChatRoom chatRoom) {
    this.message = message;
    this.sender = sender;
    this.chatRoom = chatRoom;
    this.createdAt = LocalDateTime.now();
  }

  private Message(String message, Admin adminSender, ChatRoom chatRoom) {
    this.message = message;
    this.adminSender = adminSender;
    this.chatRoom = chatRoom;
    this.createdAt = LocalDateTime.now();
  }

  public static Message of(String message, Client sender, ChatRoom chatRoom) {
    return new Message(message, sender, chatRoom);
  }

  public static Message of(String message, Admin adminSender, ChatRoom chatRoom) {
    return new Message(message, adminSender, chatRoom);
  }

  public String getSenderName() {
    if (sender != null) {
      return sender.getUsername();
    }
    if (adminSender != null) {
      return "관리자";
    }
    return "알 수 없음";
  }
}
