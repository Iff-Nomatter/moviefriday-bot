package iff.nomatter.moviefriday.kinopoisk;

import iff.nomatter.moviefriday.dto.KinopoiskGetFilmByIdResponse;
import iff.nomatter.moviefriday.dto.KinopoiskSearchByKeywordResponse;
import iff.nomatter.moviefriday.dto.MovieDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service
public class KinopoiskService {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${kinopoisk.api.url}")
    private URI kinopoiskApiUrl;

    @Value("${kinopoisk.api.key}")
    private String authToken;

    private HttpHeaders headers = new HttpHeaders();

    @PostConstruct
    public void init() {
        headers.set("X-API-KEY", authToken);
        headers.set("Content-Type", "application/json");
    }

    public KinopoiskSearchByKeywordResponse getMovieByName(String movieName) throws Exception {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<KinopoiskSearchByKeywordResponse> response = restTemplate.exchange(kinopoiskApiUrl + "search-by-keyword?keyword=" + movieName, HttpMethod.GET, entity, KinopoiskSearchByKeywordResponse.class);
        return response.getBody();
    }

    public KinopoiskGetFilmByIdResponse getMovieById(String movieId) throws Exception {
        HttpEntity<Void> entity =  new HttpEntity<>(headers);
        ResponseEntity<KinopoiskGetFilmByIdResponse> response = restTemplate.exchange(kinopoiskApiUrl + movieId, HttpMethod.GET, entity, KinopoiskGetFilmByIdResponse.class);
        return response.getBody();
    }
}
