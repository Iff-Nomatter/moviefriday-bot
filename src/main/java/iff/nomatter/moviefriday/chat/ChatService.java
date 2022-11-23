package iff.nomatter.moviefriday.chat;

import iff.nomatter.moviefriday.model.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;

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
            chatEntity.setPreviousGetRandomMovie(LocalDate.now());
            chatRepository.save(chatEntity);
            return false;
        } else if (chat.getPreviousGetRandomMovie().isAfter(LocalDate.now().minusDays(1L))) {
            return true;
        } else {
            chat.setPreviousGetRandomMovie(LocalDate.now());
            return false;
        }
    }

    public String getHowLongForNextRandomCall(Long chatId) {
        ChatEntity chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return null;
        } else {
            StringBuilder howLongForNextRandomCall = new StringBuilder();
            LocalDateTime nextRandomCall = chat.getPreviousGetRandomMovie().plusDays(1L).atStartOfDay();
            LocalDateTime requestTime = LocalDateTime.now();
            LocalDateTime tempDate = LocalDateTime.from(requestTime);
            long hours = tempDate.until(nextRandomCall, ChronoUnit.HOURS);
            tempDate = tempDate.plusHours(hours);
            howLongForNextRandomCall.append(hours).append(":");
            long minutes = tempDate.until(nextRandomCall, ChronoUnit.MINUTES);
            if (minutes <= 9) {
                howLongForNextRandomCall.append("0");
            }
            howLongForNextRandomCall.append(minutes);

            return howLongForNextRandomCall.toString();
        }
    }
}
