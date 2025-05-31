package com.chat.chattingserverapp.client.command;

import static com.chat.chattingserverapp.client.domain.Client.of;

import com.chat.chattingserverapp.client.domain.Client;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateClientCommand(
    String username,
    String password
) {

  public Client toClient(UUID id,String hashedPassword) {
    return of(id,username, hashedPassword);
  }
}
