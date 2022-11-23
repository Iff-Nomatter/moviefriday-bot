package iff.nomatter.moviefriday.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity(name = "Chat")
@Table(name = "chats")
public class ChatEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "last_call")
    private LocalDate previousGetRandomMovie;
}
