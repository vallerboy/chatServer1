package pl.oskarpolak.chat.models;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.oskarpolak.chat.models.commands.CommandFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSocket
public class ChatSocket extends TextWebSocketHandler /* BinaryWebSocketHandler */ implements WebSocketConfigurer{

    private List<UserModel> userList;
    private CommandFactory commandFactory;

    public ChatSocket() {
        userList = new ArrayList<>();
        commandFactory = new CommandFactory(userList);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this, "/chat").setAllowedOrigins("*");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // HandleTextMessage to metoda, do ktorej trafiaja wszystkie wiadomosci od
        // podlaczonych clientow

        UserModel sender = findUserModel(session);

        if(sender.getNickname() == null){
            sender.setNickname(message.getPayload());
            sender.sendMessage("Ustawiono Twój nick na " + message.getPayload());
            sendMessageToAllWithoutMe(sender, "Użytkownik " + message.getPayload() + " dołączył");
            return;
        }

        if(commandFactory.parseCommand(sender, message.getPayload())){
            return;
        }

        sendMessageToAll(generatePrefix(sender) + message.getPayload());
        sender.addGlobalMessage();
    }

    private void sendMessageToAllWithoutMe(UserModel sender, String s) {
        userList.stream()
                .filter(user -> !user.equals(sender))
                .forEach(user -> user.sendMessage(s));
    }


    private String generatePrefix(UserModel userModel) {
        return "<" + getTime() + ">" +  " " + userModel.getNickname() + ": ";
    }

    private String getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private UserModel findUserModel(WebSocketSession session) {
        return userList.stream()
                                        .filter(s -> s.getSession().equals(session))
                                        .findAny()
                                        .get();
    }


    private void sendMessageToAll(String message) {
        userList.forEach(s -> s.sendMessage(message));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UserModel sender = new UserModel(session);
        userList.add(sender);

        sender.sendMessage("Witaj w komunikatorze AkademiiKodu!");
        sender.sendMessage("Twoja pierwsza wiadomość, będzie Twoim nickiem");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UserModel userModel = findUserModel(session);
        if(userModel.getNickname() != null) {
            sendMessageToAllWithoutMe(userModel, "Użytkownik " + userModel.getNickname() + " opuścił chat");
        }
        userList.remove(userModel);
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
