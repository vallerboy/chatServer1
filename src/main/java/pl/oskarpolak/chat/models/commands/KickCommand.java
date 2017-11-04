package pl.oskarpolak.chat.models.commands;

import pl.oskarpolak.chat.models.UserModel;

public class KickCommand implements Command {
    @Override
    public void parseCommand(UserModel model, String... args) {
        String nickToKick = args[0];

    }

    @Override
    public int argsCount() {
        return 1;
    }

    @Override
    public String error() {
        return "Użycie komendy to: /kick tutaj_nick";
    }
}
