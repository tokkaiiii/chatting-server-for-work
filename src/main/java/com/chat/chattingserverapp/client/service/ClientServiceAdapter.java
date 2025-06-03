package com.chat.chattingserverapp.client.service;

import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.command.LoginClientCommand;
import com.chat.chattingserverapp.client.infrastructure.ClientRepository;
import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.response.ClientResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceAdapter implements ClientService {

  private final ClientRepository clientRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<ClientResponse> register(CreateClientCommand command) {
    String hashedPassword = passwordEncoder.encode(command.password());
    UUID id = UUID.randomUUID();

    return clientRepository.save(command.toClient(id, hashedPassword))
        .map(ClientResponse::from);
  }

  @Override
  public Optional<ClientResponse> login(LoginClientCommand command) {
    return clientRepository.findByEmail(command.email())
        .filter(client -> client.decodePassword(passwordEncoder, command.password()))
        .map(ClientResponse::from);
  }


  @Override
  public Optional<ClientResponse> findById(UUID id) {
    return clientRepository.findById(id)
        .map(ClientResponse::from);
  }

  @Override
  public Optional<ClientResponse> findByEmail(IssueClientToken query) {
    return clientRepository.findByEmail(query.email())
        .map(ClientResponse::from);

  }
}
