package iff.nomatter.moviefriday.service;

import iff.nomatter.moviefriday.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.List;

@Service
public class CommandService {

    private MovieService movieService;

    @Autowired
    public CommandService(MovieService movieService) {
        this.movieService = movieService;
    }

    public SendPhoto addMovie(String movieName, Long chatId) throws Exception {
        Movie movie = movieService.addMovieByName(movieName, chatId);
        if (movie == null) {
            File notfound = new File("src/main/resources/notfound.jpg");
            return SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(notfound))
                    .caption("Ничего не удалось найти, попробуйте добавить фильм, используя Kinopoisk ID")
                    .build();
        }
        InputFile poster = new InputFile(movie.getPosterUrlPreview());
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(poster)
                .caption("Добавлен фильм: " + movie.getNameRu() + " / " + movie.getNameEn() + "\n" +
                        "Год выхода: " + movie.getYear() + "\n" +
                        "Длительность: " + movie.getFilmLength() + "\n" +
                        movie.getDescription() + "\n" +
                        "\n" +
                        "ID: " + movie.getId() + "\n" +
                        "Если это не тот фильм - попробуйте добавить его по ID Кинопоиска.")
                .build();
    }

    public SendPhoto addMovieById(String movieId, Long chatId) throws Exception {
        Movie movie = movieService.addMovieById(movieId, chatId);
        if (movie == null) {
            File notFound = new File("src/main/resources/notfound.jpg");
            return SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(notFound))
                    .caption("Ничего не удалось найти :(")
                    .build();
        }
        InputFile poster = new InputFile(movie.getPosterUrlPreview());
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(poster)
                .caption("Добавлен фильм: " + movie.getNameRu() + " / " + movie.getNameEn() + "\n" +
                        "Год выхода: " + movie.getYear() + "\n" +
                        "Длительность: " + movie.getFilmLength() + "\n" +
                        movie.getDescription() + "\n" +
                        "\n" +
                        "ID: " + movie.getId())
                .build();
    }

    public SendMessage getAllMoviesForChat(Long chatId) {
        List<Movie> movieList = movieService.getAllMoviesForChat(chatId);
        StringBuilder message = new StringBuilder();
        for (Movie movie : movieList) {
            message.append("ID: ");
            message.append(movie.getId());
            message.append(" - ");
            message.append(movie.getNameRu());
            message.append(" / ");
            message.append(movie.getNameEn());
            message.append("\n");
        }
        return SendMessage.builder()
                .chatId(chatId)
                .text(String.valueOf(message))
                .build();
    }

    public void deleteMovieById(Long movieId) {
        movieService.deleteMovieById(movieId);
    }

    public SendPhoto getRandomMovieByChatId(Long chatId) {
        Movie movie = movieService.fetchRandomMovieByChatId(chatId);
        if (movie == null) {
            File noMoviesLeft = new File("src/main/resources/noMoviesLeft.jpg");
            return SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(noMoviesLeft))
                    .caption("Все уже просмотрено.")
                    .build();
        }
        InputFile poster = new InputFile(movie.getPosterUrlPreview());
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(poster)
                .caption("Сегодня смотрим:" + "\n" +
                        movie.getNameRu() + " / " + movie.getNameEn() + "\n" +
                        "Год выхода: " + movie.getYear() + "\n" +
                        "Длительность: " + movie.getFilmLength() + "\n" +
                        movie.getDescription() + "\n" +
                        "\n" +
                        "Приятного просмотра!"
                )
                .build();
    }
}
