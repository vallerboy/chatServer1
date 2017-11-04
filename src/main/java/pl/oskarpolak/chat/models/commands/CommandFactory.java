package pl.oskarpolak.chat.models.commands;

import pl.oskarpolak.chat.models.UserModel;

import java.util.Arrays;
import java.util.Map;

public class CommandFactory {

    private static Map<String, Command> stringCommandMap;

    static  {
        stringCommandMap.put("command", new KickCommand());
    }

    public boolean parseCommand(UserModel userModel, String s){
        if(!s.startsWith("/")){
            return false;
        }
        // /kick Oskar
        String[] parts = s.split(" ");
        String[] args = Arrays.copyOfRange(parts, 1, parts.length+1);
        String commandAlone = parts[0].substring(1, parts[0].length());
        if(!stringCommandMap.containsKey(commandAlone)){
            userModel.sendMessage("Taka komenda nie istnieje!");
            return true;
        }

        Command command = stringCommandMap.get(commandAlone);
        if(command.argsCount() != parts.length /* ilosc indexow jest od zera */){
            userModel.sendMessage(command.error());
        }

        command.parseCommand(userModel, args);
        return true;
    }
}
