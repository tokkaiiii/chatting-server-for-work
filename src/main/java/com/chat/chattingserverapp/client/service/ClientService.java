package com.chat.chattingserverapp.client.service;

import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.command.LoginClientCommand;
import com.chat.chattingserverapp.client.response.ClientResponse;

public interface ClientService {

  ClientResponse register(CreateClientCommand command);

  ClientResponse login(LoginClientCommand command);
}
