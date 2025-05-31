package com.chat.chattingserverapp.client.service;

import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.command.LoginClientCommand;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.client.infrastructure.ClientRepository;
import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.response.ClientResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceAdapter implements ClientService{
  private final ClientRepository clientRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ClientResponse register(CreateClientCommand command) {
    String hashedPassword = passwordEncoder.encode(command.password());
    Optional<Client> save = clientRepository.save(command.toClient(hashedPassword));
    return save
        .map(client -> new ClientResponse(client.getId(),client.getUsername(), client.getCreatedAt()))
        .orElseThrow(() -> new RuntimeException("Failed to register client"));
  }

  @Override
  public ClientResponse login(LoginClientCommand command) {
    Optional<Client> byUsername = clientRepository.findByUsername(command.username());
    return clientRepository.findByUsername(command.username())
        .filter(client -> client.decodePassword(passwordEncoder, command.password()))
        .map(client -> new ClientResponse(client.getId(), client.getUsername(), client.getCreatedAt()))
        .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
  }

  @Override
  public Optional<ClientResponse> findByUsername(IssueClientToken query) {
    return clientRepository.findByUsername(query.username())
        .filter(client -> client.decodePassword(passwordEncoder, query.password()))
        .map(client -> new ClientResponse(client.getId(), client.getUsername(),
            client.getCreatedAt()));
  }
}
