package pl.oskarpolak.chat.models.commands;

import pl.oskarpolak.chat.models.UserModel;

import java.util.List;

public class MeCommand implements Command {
    @Override
    public void parseCommand(UserModel model, List<UserModel> userList, String... args) {
        model.sendMessage("~ Liczba prywatnych wiadomości: " + model.getSentPrivate());
        model.sendMessage("~ Liczba globalnych wiadomości: " + model.getSentGlobal());
    }

    @Override
    public int argsCount() {
        return 0;
    }

    @Override
    public String error() {
        return "/me ";
    }
}
