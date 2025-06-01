package com.chat.chattingserverapp.client.command;

import static com.chat.chattingserverapp.client.domain.Client.of;

import com.chat.chattingserverapp.client.domain.Client;
import java.util.UUID;

public record CreateClientCommand(
    String email,
    String username,
    String password
) {

  public Client toClient(UUID id, String hashedPassword) {
    return of(id, email, username, hashedPassword);
  }
}
