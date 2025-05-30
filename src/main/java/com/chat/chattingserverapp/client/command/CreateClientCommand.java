package com.chat.chattingserverapp.client.command;

import static com.chat.chattingserverapp.client.domain.Client.of;

import com.chat.chattingserverapp.client.domain.Client;
import java.time.LocalDateTime;

public record CreateClientCommand(
    String username,
    String password
) {

  public Client toClient(String hashedPassword) {
    return of(username, hashedPassword);
  }
}
