package iff.nomatter.moviefriday.bot;

import iff.nomatter.moviefriday.chat.ChatService;
import iff.nomatter.moviefriday.model.Movie;
import iff.nomatter.moviefriday.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
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
                || !update.getMessage().hasText()
                || !update.getMessage().isCommand()
                || !update.getMessage().getText().contains(botName)) {
            return;
        }
        String command = String.valueOf(BotCommand.getCommand(update.getMessage().getText()));
        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        switch (command) {
            case ("ADDMOVIEBYNAME"):
                String movieName = messageText.substring(messageText.indexOf(" ") + 1);
                try {
                   SendPhoto message = commandService.addMovie(movieName, chatId);
                   sendPhoto(message);
                } catch (Exception e) {
                    sendSimpleMessage(update.getMessage().getChatId(), "Не получилось :(");
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
                break;
            case ("ADDMOVIEBYID"):
                String movieKinopoiskId = messageText.substring(messageText.indexOf(" ") + 1);
                try {
                    SendPhoto message = commandService.addMovieById(movieKinopoiskId, chatId);
                    sendPhoto(message);
                } catch (Exception e) {
                    sendSimpleMessage(update.getMessage().getChatId(), "Не получилось :(");
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
                break;
            case ("GETRANDOM"):
                if (!chatService.wasRandomCalledToday(chatId)) {
                    try {
                        SendPhoto message = commandService.getRandomMovieByChatId(chatId);
                        sendPhoto(message);
                    } catch (Exception e) {
                        sendSimpleMessage(update.getMessage().getChatId(), "Не получилось :(");
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                } else {
                    sendSimpleMessage(chatId, "Вы сегодня уже получили фильм для просмотра.");
                }
                break;
            case ("GETALL"):
                SendMessage message = commandService.getAllMoviesForChat(chatId);
                sendMessage(message);
                break;
            case ("DELETEBYID"):
                String movieId = messageText.substring(messageText.indexOf(" ") + 1);
                commandService.deleteMovieById(Long.valueOf(movieId));
                sendSimpleMessage(update.getMessage().getChatId(), "Удалено. Теперь список фильмов для этого чата выглядит так:" + "\n");
                SendMessage movieList = commandService.getAllMoviesForChat(chatId);
                sendMessage(movieList);
                break;
            default:
                sendSimpleMessage(update.getMessage().getChatId(), "Сорян :(");
                log.info("No such command");
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
