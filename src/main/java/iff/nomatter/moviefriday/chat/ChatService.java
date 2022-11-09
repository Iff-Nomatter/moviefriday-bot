package iff.nomatter.moviefriday.chat;

import iff.nomatter.moviefriday.model.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional
    public boolean wasRandomCalledToday(Long chatId) {
        ChatEntity chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setId(chatId);
            chatEntity.setPreviousGetRandomMovie(LocalDateTime.now());
            chatRepository.save(chatEntity);
            return false;
        } else if (chat.getPreviousGetRandomMovie().isAfter(LocalDateTime.now().minusDays(1L))) {
            return true;
        } else {
            chat.setPreviousGetRandomMovie(LocalDateTime.now());
            return false;
        }
    }
}
