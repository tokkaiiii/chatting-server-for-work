package com.chat.chattingserverapp.client.infrastructure;

import static java.util.Optional.of;

import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.common.infrastructure.SpringDataClientRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepository{
  private final SpringDataClientRepository jpaClientRepository;

  @Override
  public Optional<Client> save(Client client) {
    return of(jpaClientRepository.save(client));
  }

  @Override
  public Optional<Client> findByUsername(String username) {
    return jpaClientRepository.findByUsername(username);
  }

  @Override
  public List<Client> findAll() {
    return jpaClientRepository.findAll();
  }

  @Override
  public Optional<Client> findById(UUID id) {
    return jpaClientRepository.findById(id);
  }

}
