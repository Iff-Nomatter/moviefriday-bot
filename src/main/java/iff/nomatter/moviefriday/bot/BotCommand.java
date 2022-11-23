package iff.nomatter.moviefriday.bot;

import java.util.Arrays;

public enum BotCommand {
    ADDMOVIEBYNAME("добавитьфильм", "Добавляет фильм в список кинопятницы для этого чата. Для ее использования через пробел после команды введите поисковый запрос. Кинопоиск вернет первый подходящий по этому запросу фильм. Например: !добавитьфильм шрек 2001"),
    ADDMOVIEBYID("добавитьфильмпоid", "Добавляет фильм в список кинопятницы для этого чата. Для ее использования через пробел после команды введите ID Кинопоиска. Например: !добавитьфильмпоid 301"),
    GETRANDOM("случайныйфильм", "Выдает случайный фильм для просмотра на кинопятницу. Команду можно вызывать только раз в день."),
    GETALL("список", "Выводит список фильмов на кинопятницу для этого чата."),
    DELETEBYID("удалитьпоid", "Удаляет фильм из списка для этого чата. Через пробел после команды нужно ввести внутренний ID фильма, его можно узнать, получив список фильмов, а также он пишется при добавлении фильма."),
    HELP("хелп", "Вывод справки по командам.");

    public String commandName;
    public String commandDescription;

    BotCommand(String commandName, String commandDescription) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
    }

    public static BotCommand getCommand(String messageText) {
        String commandNameFromMessage = messageText.toLowerCase();
        if (messageText.contains(" ")) {
            commandNameFromMessage = messageText.substring(1, messageText.indexOf(" "));
        } else {
            commandNameFromMessage = commandNameFromMessage.substring(1);
        }
        String finalCommandNameFromMessage = commandNameFromMessage;
        return Arrays.stream(BotCommand.values())
                .filter(c -> c.commandName.equals(finalCommandNameFromMessage))
                .findFirst()
                .orElse(null);
    }
}


