package com.chat.chattingserverapp.websocket.event;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Slf4j
public class StompEventListener {

  private final ConcurrentHashMap<String, String> sessionMap = new ConcurrentHashMap<>();

  public Set<String> getSessions() {
    return sessionMap.keySet();
  }

  @EventListener
  public void listener(SessionConnectEvent event) {
    log.info("SessionConnectEvent. {}", event);

  }

  @EventListener
  public void listener(SessionConnectedEvent event) {
    log.info("SessionConnectedEvent. {}", event);
    String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
    sessionMap.put(sessionId, sessionId);
  }

  @EventListener
  public void listener(SessionSubscribeEvent event) {
    log.info("SessionSubscribeEvent. {}", event);
  }

  @EventListener
  public void listener(SessionUnsubscribeEvent event) {
    log.info("SessionUnsubscribeEvent. {}", event);
  }

  @EventListener
  public void listener(SessionDisconnectEvent event) {
    log.info("SessionDisconnectEvent. {}", event);
    String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
    sessionMap.remove(sessionId);
  }
}
