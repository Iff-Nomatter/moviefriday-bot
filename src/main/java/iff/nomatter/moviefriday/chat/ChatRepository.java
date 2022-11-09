package iff.nomatter.moviefriday.chat;

import iff.nomatter.moviefriday.model.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
}
