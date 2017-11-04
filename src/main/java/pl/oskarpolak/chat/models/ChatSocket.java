package pl.oskarpolak.chat.models;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@EnableWebSocket
public class ChatSocket extends TextWebSocketHandler /* BinaryWebSocketHandler */ implements WebSocketConfigurer{

    List<UserModel> userList;

    public ChatSocket() {
        userList = new ArrayList<>();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this, "/chat").setAllowedOrigins("*");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // HandleTextMessage to metoda, do ktorej trafiaja wszystkie wiadomosci od
        // podlaczonych clientow

        
        sendMessageToAll(message.getPayload() + "\n");
    }



    private void sendMessageToAll(String message) {
        userList.forEach(s -> s.sendMessage(message));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        userList.add(new UserModel(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        userList.remove(userList.stream()
                .filter(s -> s.getSession().equals(session))
                .findAny()
                .get());
        // UserModel == WebSocketSession? NIE
        // UserModel == UserModel? tak
        // Przeszukuje liste od UserModel, która zawiera WebSocketSession
        // i szukam usera o takiej samej sesji
        // jesli znajde, zwracam go, a nastepnie usuwam.

        // REMOVE: -> UserModel3 (model przyblizony)
        //UserModel1 == UserModel3? NIE
        //UserModel2 == UserModel3? NIE
        //UserModel3 == UserModel3? TAK -> USUN
        //UserModel4


    }
}
