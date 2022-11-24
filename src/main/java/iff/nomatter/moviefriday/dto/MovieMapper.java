package iff.nomatter.moviefriday.dto;

import iff.nomatter.moviefriday.model.Movie;

public class MovieMapper {
    public static Movie DtoToMovie(MovieDto movieDto, Long chatId) {
        Movie movie = new Movie();
        movie.setYear(movieDto.getYear());
        movie.setFilmLength(movieDto.getFilmLength());
        movie.setNameRu(movieDto.getNameRu());
        movie.setNameEn(movieDto.getNameEn());
        movie.setPosterUrlPreview(movieDto.getPosterUrlPreview());
        movie.setDescription(movieDto.getDescription().length() > 500 ? movieDto.getDescription().substring(0, 500) : movieDto.getDescription());
        movie.setChatId(chatId);
        return movie;
    }
}