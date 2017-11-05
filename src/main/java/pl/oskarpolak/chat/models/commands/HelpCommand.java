package pl.oskarpolak.chat.models.commands;

import pl.oskarpolak.chat.models.UserModel;

import java.util.List;
import java.util.Map;

public class HelpCommand implements Command{
    @Override
    public void parseCommand(UserModel model, List<UserModel> userList, String... args) {
        model.sendMessagePacket("Lista komend: ");
        for (Map.Entry<String, Command> stringCommandEntry : CommandFactory.getCommandMap().entrySet()) {
            model.sendMessagePacket("~ " + stringCommandEntry.getKey() + " - " + stringCommandEntry.getValue().error());
        }

//        for (String key : CommandFactory.getCommandMap().keySet()) {
//            model.sendMessage("~ " + key + " - " + CommandFactory.getCommandMap().get(key));
//        }
    }

    @Override
    public int argsCount() {
        return 0;
    }

    @Override
    public String error() {
        return "/help";
    }
}
