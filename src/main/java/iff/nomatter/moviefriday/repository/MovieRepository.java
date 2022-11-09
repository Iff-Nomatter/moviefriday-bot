package iff.nomatter.moviefriday.repository;

import iff.nomatter.moviefriday.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "select * from movies where chat_id = ?1 order by random() limit 1", nativeQuery = true)
    Movie fetchRandomMovieByChatId(Long chatId);

    List<Movie> findAllByChatId(Long chatId);

    void deleteMovieById(Long id);
}
