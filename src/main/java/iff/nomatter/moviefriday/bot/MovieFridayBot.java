package iff.nomatter.moviefriday.bot;

import iff.nomatter.moviefriday.chat.ChatService;
import iff.nomatter.moviefriday.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MovieFridayBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private CommandService commandService;
    private ChatService chatService;

    @Autowired
    public MovieFridayBot(CommandService commandService,
                          ChatService chatService) {
        this.commandService = commandService;
        this.chatService = chatService;
    }

    @PostConstruct
    private void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
        log.info("Bot registered");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update == null) {
            return;
        }
        if (!update.hasMessage()
                || !update.getMessage().hasText()) {
            return;
        }
        if (update.getMessage().getText().startsWith("!")) {
            BotCommand command = BotCommand.getCommand(update.getMessage().getText());
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (command == null) {
                sendSimpleMessage(chatId, "Не понимаю :(");
                log.info("No such command");
                return;
            }
            try {
                processCommand(command, messageText, chatId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processCommand(BotCommand command, String messageText, Long chatId) throws Exception {
        switch (command) {
            case ADDMOVIEBYNAME:
                String movieName = messageText.substring(messageText.indexOf(" ") + 1);
                sendPhoto(commandService.addMovie(movieName, chatId));
                break;
            case ADDMOVIEBYID:
                String movieKinopoiskId = messageText.substring(messageText.indexOf(" ") + 1);
                sendPhoto(commandService.addMovieById(movieKinopoiskId, chatId));
                break;
            case GETRANDOM:
                if (!chatService.wasRandomCalledToday(chatId)) {
                    sendPhoto(commandService.getRandomMovieByChatId(chatId));
                } else {
                    sendSimpleMessage(chatId, "Вы сегодня уже получили фильм для просмотра. \nДо следующего запроса осталось: " + chatService.getHowLongForNextRandomCall(chatId));
                }
                break;
            case GETALL:
                sendMessage(commandService.getAllMoviesForChat(chatId));
                break;
            case DELETEBYID:
                String movieId = messageText.substring(messageText.indexOf(" ") + 1);
                commandService.deleteMovieById(Long.valueOf(movieId));
                sendSimpleMessage(chatId, "Удалено. Теперь список фильмов для этого чата выглядит так:" + "\n");
                sendMessage(commandService.getAllMoviesForChat(chatId));
                break;
            case HELP:
                StringBuilder helpMessageBuilder = new StringBuilder();
                helpMessageBuilder.append("Список команд бота: ").append("\n").append("\n");
                for (BotCommand botCommand : BotCommand.values()) {
                    helpMessageBuilder.append("!");
                    helpMessageBuilder.append(botCommand.commandName);
                    helpMessageBuilder.append("\n");
                    helpMessageBuilder.append(botCommand.commandDescription);
                    helpMessageBuilder.append("\n");
                    helpMessageBuilder.append("\n");
                }
                sendSimpleMessage(chatId, helpMessageBuilder.toString());
                break;
        }
    }

    private void sendSimpleMessage(Long chatId, String message) {
        SendMessage simpleMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        sendMessage(simpleMessage);
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(SendPhoto message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
