package pl.oskarpolak.chat.models.commands;

import pl.oskarpolak.chat.models.UserModel;

public interface Command {
    void parseCommand(UserModel model, String ... args);
    int argsCount();
    String error();
}
