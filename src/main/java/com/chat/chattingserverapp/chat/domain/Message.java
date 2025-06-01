package com.chat.chattingserverapp.chat.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.chat.chattingserverapp.client.domain.Client;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Message {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String message;

  @JoinColumn(name = "sender_id")
  @ManyToOne(fetch = LAZY)
  private Client sender;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;

  protected Message() {
  }

  private Message(String message, Client sender, ChatRoom chatRoom) {
    this.message = message;
    this.sender = sender;
    this.chatRoom = chatRoom;
  }

  public static Message of(String message, Client sender, ChatRoom chatRoom) {
    return new Message(message, sender, chatRoom);
  }

}
