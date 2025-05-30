package com.chat.chattingserverapp.client.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Client {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Getter
  @Column(unique = true)
  private String username;

  private Client(String username) {
    this.username = username;
  }

  public static Client of(String username) {
    return new Client(username);
  }

}
