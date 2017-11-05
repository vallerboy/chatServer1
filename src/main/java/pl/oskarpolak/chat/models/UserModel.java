package pl.oskarpolak.chat.models;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import pl.oskarpolak.chat.models.sockets.ChatSocket;

import java.io.IOException;

public class UserModel {
    private String nickname;
    private WebSocketSession session;

    private int sentPrivate;
    private int sentGlobal;


    public UserModel(WebSocketSession session) {
        this.session = session;
        this.nickname = null;
        //todo convert nickname to Optional
    }

    public int getSentPrivate() {
        return sentPrivate;
    }

    public int getSentGlobal() {
        return sentGlobal;
    }

    public void addPrivateMessage(){
        sentPrivate += 1;
    }

    public void addGlobalMessage(){
        sentGlobal += 1;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModel userModel = (UserModel) o;

        if (nickname != null ? !nickname.equals(userModel.nickname) : userModel.nickname != null) return false;
        return session != null ? session.equals(userModel.session) : userModel.session == null;
    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (session != null ? session.hashCode() : 0);
        return result;
    }

    public void sendMessagePacket(String message) {
        MessageModel messageModel = new MessageModel();
        messageModel.setMessageType(MessageModel.MessageType.MESSAGE);
        messageModel.setContext(message + "\n");
        try {
            session.sendMessage(new TextMessage(ChatSocket.GSON.toJson(messageModel)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDialogPacket(String message) {
        MessageModel messageModel = new MessageModel();
        messageModel.setMessageType(MessageModel.MessageType.OPEN_DIALOG);
        messageModel.setContext(message);
        try {
            session.sendMessage(new TextMessage(ChatSocket.GSON.toJson(messageModel)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCloseWindowPacket() {
        MessageModel messageModel = new MessageModel();
        messageModel.setMessageType(MessageModel.MessageType.CLOSE_WINDOW);
        try {
            session.sendMessage(new TextMessage(ChatSocket.GSON.toJson(messageModel)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
