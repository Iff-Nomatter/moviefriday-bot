package iff.nomatter.moviefriday.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KinopoiskGetFilmByIdResponse {
    @JsonProperty("data")
    private MovieDto data;
}
