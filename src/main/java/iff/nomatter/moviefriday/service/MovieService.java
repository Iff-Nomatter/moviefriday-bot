package iff.nomatter.moviefriday.service;

import iff.nomatter.moviefriday.dto.KinopoiskSearchByKeywordResponse;
import iff.nomatter.moviefriday.dto.MovieDto;
import iff.nomatter.moviefriday.dto.MovieMapper;
import iff.nomatter.moviefriday.kinopoisk.KinopoiskService;
import iff.nomatter.moviefriday.model.Movie;
import iff.nomatter.moviefriday.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final KinopoiskService kinopoiskService;
    private final MovieRepository movieRepository;

    @Transactional
    public Movie addMovieByName(String movieName, Long chatId) throws Exception {
        KinopoiskSearchByKeywordResponse movieList = kinopoiskService.getMovieByName(movieName);
        if (movieList.getFilms().isEmpty()) {
            return null;
        }
        Movie movie = MovieMapper.DtoToMovie(movieList.getFilms().get(0), chatId);
        movieRepository.save(movie);
        return movie;
    }

    @Transactional
    public Movie addMovieById(String movieId, Long chatId) throws Exception {
        MovieDto movieDto = kinopoiskService.getMovieById(movieId).getData();
        Movie movie = MovieMapper.DtoToMovie(movieDto, chatId);
        movieRepository.save(movie);
        return movie;
    }

    public List<Movie> getAllMoviesForChat(Long chatId) {
        return movieRepository.findAllByChatId(chatId);
    }

    @Transactional
    public void deleteMovieById(Long movieId) {
        movieRepository.deleteMovieById(movieId);
    }

    @Transactional
    public Movie fetchRandomMovieByChatId(Long chatId) {
        Movie movie = movieRepository.fetchRandomMovieByChatId(chatId);
        if (movie == null) {
            return movie;
        }
        deleteMovieById(movie.getId());
        return movie;
    }
}
