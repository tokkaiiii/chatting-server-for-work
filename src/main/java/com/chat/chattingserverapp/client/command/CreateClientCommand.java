package com.chat.chattingserverapp.client.command;

import com.chat.chattingserverapp.client.domain.Client;

public record CreateClientCommand(
    String username,
    String password
) {

  public Client toClient() {
    return Client.of(username);
  }
}
