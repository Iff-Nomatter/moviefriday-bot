package iff.nomatter.moviefriday.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "Movie")
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "poster_url")
    private String posterUrlPreview;

    @Column(name = "year")
    private Integer year;

    @Column(name = "length")
    private String filmLength;

    @Column(name = "description")
    private String description;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;
}
