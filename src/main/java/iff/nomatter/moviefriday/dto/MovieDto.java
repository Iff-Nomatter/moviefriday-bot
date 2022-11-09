package iff.nomatter.moviefriday.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MovieDto {
    @JsonProperty("posterUrlPreview")
    private String posterUrlPreview;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("filmLength")
    private String filmLength;

    @JsonProperty("description")
    private String description;

    @JsonProperty("nameRu")
    private String nameRu;

    @JsonProperty("nameEn")
    private String nameEn;
}
