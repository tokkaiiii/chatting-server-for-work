package com.chat.chattingserverapp.chat.domain;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.chat.chattingserverapp.client.domain.Client;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
public class ChatRoom {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(unique = true)
  @Getter
  private String roomName;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "client_id")
  private Client client;

  protected ChatRoom() {
  }

  private ChatRoom(String roomName, Client client) {
    this.roomName = roomName;
  }

  public static ChatRoom of(String roomName, Client client) {
    return new ChatRoom(roomName, client);
  }

}
