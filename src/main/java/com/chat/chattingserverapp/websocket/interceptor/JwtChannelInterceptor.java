package com.chat.chattingserverapp.websocket.interceptor;

import java.util.List;
import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public record JwtChannelInterceptor(
    JwtDecoder jwtDecoder
) implements ChannelInterceptor {

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
    String token = accessor.getFirstNativeHeader("Authorization");

      if (token == null || token.isEmpty()) {
        // 👉 쿼리 파라미터에서 가져오기
        Map<String, List<String>> parameters =
            (Map<String, List<String>>) accessor.getSessionAttributes()
                .get("javax.websocket.endpoint.parameters");

        if (parameters != null && parameters.containsKey("access_token")) {
          token = parameters.get("access_token").get(0);
        }
      }

      if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
      }

      // 🔐 여기서 JWT 파싱 및 인증 세션 설정
      // SecurityContextHolder.getContext().setAuthentication(...);
      // 예시: JWT 토큰을 검증하고 사용자 정보를 가져오는 로직
      if (token == null || token.isEmpty()) {
        throw new IllegalArgumentException("JWT token is missing or invalid");
      }

      try {
        Jwt jwt = jwtDecoder.decode(token);
        // 예시: JWT 토큰을 검증하고 사용자 정보를 가져오는 로직
        accessor.setUser(new JwtAuthenticationToken(jwt, null, jwt.getSubject()));

      } catch (JwtException e) {
        throw new IllegalArgumentException("JWT token is missing or invalid");
      }
      // 예시: JWT 토큰을 검증하고 사용자 정보를 가져오는 로직
      // 예시: SecurityContextHolder.getContext().setAuthentication(authentication);
      // 예시: 인증된 사용자 정보를 세션에 저장
      // 예시: accessor.setUser(authentication.getPrincipal());
      // 예시: accessor.setSessionAttributes(Map.of("user", authentication.getPrincipal()));
    } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
      // 🔒 연결 해제 시 세션 정리
      // 예시: SecurityContextHolder.clearContext();
      // 예시: accessor.getSessionAttributes().clear();
      // 예시: accessor.setUser(null);
    } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
      // 구독 요청 처리
      // 예시: 구독 토픽에 대한 권한 검사
      // 예시: accessor.setSubscriptionId(...);
    } else if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {
      // 구독 해제 처리
      // 예시: 구독 ID를 사용하여 세션에서 구독 제거
    }
    // 메시지 헤더에 사용자 정보 추가
    // 예시: accessor.setHeader("user", user);
    // 메시지 헤더에 인증 정보 추가
    // 예시: accessor.setHeader("auth", authentication);
    // 메시지 헤더에 세션 정보 추가
    // 예시: accessor.setSessionAttributes(Map.of("session", session));
    // 메시지 헤더에 추가 정보 설정
    // 예시: accessor.setHeader("customHeader", "customValue");
    // 메시지 헤더에 토큰 정보 설정
    // 예시: accessor.setHeader("token", token);
    // 메시지 헤더에 클라이언트 정보 설정
    // 예시: accessor.setHeader("client", clientInfo);
    // 메시지 헤더에 요청 정보 설정
    // 예시: accessor.setHeader("request", requestInfo);
    // 메시지 헤더에 응답 정보 설정
    // 예시: accessor.setHeader("response", responseInfo);
    // 메시지 헤더에 추가적인 메타데이터 설정
    // 예시: accessor.setHeader("metadata", metadata);
    // 메시지 헤더에 클라이언트 ID 설정
    // 예시: accessor.setHeader("clientId", clientId);
    // 메시지 헤더에 세션 ID 설정
    // 예시: accessor.setHeader("sessionId", sessionId);
    // 메시지 헤더에 요청 ID 설정
    // 예시: accessor.setHeader("requestId", requestId);
    // 메시지 헤더에 응답 ID 설정

    return message;
  }

}
