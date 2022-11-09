package iff.nomatter.moviefriday.dto;

import lombok.Data;

import java.util.List;

@Data
public class KinopoiskSearchByKeywordResponse {
    private List<MovieDto> films;
}
