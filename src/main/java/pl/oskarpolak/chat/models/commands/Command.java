package pl.oskarpolak.chat.models.commands;

import pl.oskarpolak.chat.models.UserModel;

import java.util.List;

public interface Command {
    void parseCommand(UserModel model, List<UserModel> userList, String ... args);
    int argsCount();
    String error();
}
