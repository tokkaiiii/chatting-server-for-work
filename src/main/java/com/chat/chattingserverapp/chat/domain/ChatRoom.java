package com.chat.chattingserverapp.chat.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
public class ChatRoom {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(unique = true)
  @Getter
  private String roomName;

  protected ChatRoom() {
  }

  private ChatRoom(String roomName) {
    this.roomName = roomName;
  }

  public static ChatRoom of(String roomName) {
    return new ChatRoom(roomName);
  }

}
