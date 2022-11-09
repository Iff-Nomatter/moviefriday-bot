package iff.nomatter.moviefriday.bot;

import java.util.Arrays;

public enum BotCommand {
    ADDMOVIEBYNAME("addmovie"),
    ADDMOVIEBYID("addmoviebyid"),
    GETRANDOM("getrandommovie"),
    GETALL("getall"),
    DELETEBYID("deletebyid");

    private String commandName;

    private BotCommand(String commandName) {
        this.commandName = commandName;
    }

    public static BotCommand getCommand(String messageText) {
        return Arrays.stream(BotCommand.values())
                .filter(c -> c.commandName.equals(messageText.substring(1, messageText.indexOf("@")).toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}


